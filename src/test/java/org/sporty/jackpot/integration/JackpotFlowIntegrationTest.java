package org.sporty.jackpot.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.support.TransactionTemplate;
import org.sporty.jackpot.repository.BetEvaluationRepository;
import org.sporty.jackpot.repository.BetRepository;
import org.sporty.jackpot.repository.JackpotContributionRepository;
import org.sporty.jackpot.repository.JackpotRepository;
import org.sporty.jackpot.repository.OutboxRepository;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.kafka.KafkaContainer;
import org.testcontainers.utility.DockerImageName;
import tools.jackson.databind.json.JsonMapper;

import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Testcontainers
class JackpotFlowIntegrationTest {

    @Container
    @ServiceConnection
    static KafkaContainer kafkaContainer = new KafkaContainer(
            DockerImageName.parse("apache/kafka-native:latest")
    );

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JsonMapper jsonMapper;

    @Autowired
    private BetRepository betRepository;

    @Autowired
    private OutboxRepository outboxRepository;

    @Autowired
    private JackpotContributionRepository jackpotContributionRepository;

    @Autowired
    private JackpotRepository jackpotRepository;

    @Autowired
    private BetEvaluationRepository betEvaluationRepository;

    @Autowired
    private TransactionTemplate transactionTemplate;

    @Test
    void createBet_persistsBetAndOutbox() throws Exception {
        var betId = "bet-" + UUID.randomUUID();

        mockMvc.perform(post("/api/jackpot/bet")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonMapper.writeValueAsString(Map.of(
                                "betId", betId,
                                "userId", "user-1",
                                "jackpotId", "mega",
                                "betAmount", 100
                        ))))
                .andExpect(status().is(equalTo(200)))
                .andExpect(jsonPath("$.betId", is(betId)));

        assertThat(betRepository.findBetByBetId(betId)).isPresent();
        assertThat(outboxRepository.findUnpublished())
                .anyMatch(event -> betId.equals(event.getAggregateId()));
    }

    @Test
    void contributionPipeline_updatesJackpotPool() throws Exception {
        var betId = "bet-" + UUID.randomUUID();
        var initialPool = findJackpotPool("mega");

        createBet(betId, "mega", 100);

        await().atMost(java.time.Duration.ofSeconds(15)).untilAsserted(() ->
                assertThat(jackpotContributionRepository.findByBetId(betId)).isPresent()
        );

        var updatedPool = findJackpotPool("mega");

        assertThat(updatedPool).isEqualByComparingTo(initialPool.add(new BigDecimal("5.00")));
    }

    @Test
    void evaluate_beforeContribution_returns404() throws Exception {
        var betId = "bet-" + UUID.randomUUID();
        createBet(betId, "mega", 100);

        mockMvc.perform(post("/api/jackpot/bet/{betId}", betId))
                .andExpect(status().is(equalTo(404)));
    }

    @Test
    void evaluate_afterContribution_returnsResult() throws Exception {
        var betId = "bet-" + UUID.randomUUID();
        createBet(betId, "mega", 100);

        awaitContribution(betId);

        mockMvc.perform(post("/api/jackpot/bet/{betId}", betId))
                .andExpect(status().is(equalTo(200)))
                .andExpect(jsonPath("$.winner", notNullValue()));
    }

    @Test
    void contributionPipeline_autoEvaluatesBet() throws Exception {
        var betId = "bet-" + UUID.randomUUID();
        createBet(betId, "mega", 100);

        await().atMost(java.time.Duration.ofSeconds(15)).untilAsserted(() ->
                assertThat(betEvaluationRepository.findByBetId(betId)).isPresent()
        );
    }

    @Test
    void evaluate_win_resetsJackpotPool() throws Exception {
        var betId = "bet-" + UUID.randomUUID();

        transactionTemplate.executeWithoutResult(status -> {
            var jackpot = jackpotRepository.findJackpotByJackpotId("super").orElseThrow();
            jackpot.setCurrentPool(new BigDecimal("100000.00"));
            jackpotRepository.saveAndFlush(jackpot);
        });

        createBet(betId, "super", 100);
        awaitContribution(betId);

        assertThat(findJackpotPool("super")).isEqualByComparingTo("500.00");

        mockMvc.perform(post("/api/jackpot/bet/{betId}", betId))
                .andExpect(status().is(equalTo(200)))
                .andExpect(jsonPath("$.winner", is(true)))
                .andExpect(jsonPath("$.rewardAmount", notNullValue()));
    }

    @Test
    void evaluate_twice_returnsSameResult() throws Exception {
        var betId = "bet-" + UUID.randomUUID();
        createBet(betId, "mega", 100);
        awaitContribution(betId);

        var firstResponse = mockMvc.perform(post("/api/jackpot/bet/{betId}", betId))
                .andExpect(status().is(equalTo(200)))
                .andReturn()
                .getResponse()
                .getContentAsString();

        mockMvc.perform(post("/api/jackpot/bet/{betId}", betId))
                .andExpect(status().is(equalTo(200)))
                .andExpect(content().json(firstResponse));
    }

    @Test
    void createBet_duplicate_returns400() throws Exception {
        var betId = "bet-" + UUID.randomUUID();
        var body = jsonMapper.writeValueAsString(Map.of(
                "betId", betId,
                "userId", "user-1",
                "jackpotId", "mega",
                "betAmount", 100
        ));

        mockMvc.perform(post("/api/jackpot/bet")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().is(equalTo(200)));

        mockMvc.perform(post("/api/jackpot/bet")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().is(equalTo(400)));
    }

    private void createBet(String betId, String jackpotId, int amount) throws Exception {
        mockMvc.perform(post("/api/jackpot/bet")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonMapper.writeValueAsString(Map.of(
                                "betId", betId,
                                "userId", "user-1",
                                "jackpotId", jackpotId,
                                "betAmount", amount
                        ))))
                .andExpect(status().is(equalTo(200)));
    }

    private void awaitContribution(String betId) {
        await().atMost(java.time.Duration.ofSeconds(15)).untilAsserted(() ->
                assertThat(jackpotContributionRepository.findByBetId(betId)).isPresent()
        );
    }

    private BigDecimal findJackpotPool(String jackpotId) {
        return transactionTemplate.execute(status ->
                jackpotRepository.findJackpotByJackpotId(jackpotId)
                        .orElseThrow()
                        .getCurrentPool()
        );
    }
}
