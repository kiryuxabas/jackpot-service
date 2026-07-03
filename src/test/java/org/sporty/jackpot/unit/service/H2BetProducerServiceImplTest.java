package org.sporty.jackpot.unit.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.sporty.jackpot.exception.BetAlreadyExistsException;
import org.sporty.jackpot.exception.JackpotNotFoundException;
import org.sporty.jackpot.model.Bet;
import org.sporty.jackpot.model.ContributionType;
import org.sporty.jackpot.model.Jackpot;
import org.sporty.jackpot.model.RewardType;
import org.sporty.jackpot.observability.JackpotMetrics;
import org.sporty.jackpot.repository.BetRepository;
import org.sporty.jackpot.repository.JackpotRepository;
import org.sporty.jackpot.repository.OutboxRepository;
import org.sporty.jackpot.service.command.CreateBetCommand;
import org.sporty.jackpot.service.impl.H2BetProducerServiceImpl;
import tools.jackson.databind.json.JsonMapper;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class H2BetProducerServiceImplTest {

    @Mock
    private BetRepository betRepository;
    @Mock
    private OutboxRepository outboxRepository;
    @Mock
    private JsonMapper jsonMapper;
    @Mock
    private JackpotRepository jackpotRepository;
    @Mock
    private JackpotMetrics jackpotMetrics;

    @InjectMocks
    private H2BetProducerServiceImpl betProducerService;

    @Test
    void produceBet_success_persistsBetAndOutbox() throws Exception {
        var command = new CreateBetCommand("bet-1", "user-1", "mega", new BigDecimal("100.00"));
        var jackpot = Jackpot.valueOf(
                "mega",
                new BigDecimal("1000.00"),
                new BigDecimal("1000.00"),
                ContributionType.FIXED,
                RewardType.FIXED
        );
        var savedBet = Bet.valueOf("bet-1", "user-1", "mega", new BigDecimal("100.00"));

        when(jackpotRepository.findJackpotByJackpotId("mega")).thenReturn(Optional.of(jackpot));
        when(betRepository.saveAndFlush(any(Bet.class))).thenReturn(savedBet);
        when(jsonMapper.writeValueAsString(any())).thenReturn("{\"betId\":\"bet-1\"}");

        var betId = betProducerService.produceBet(command);

        assertThat(betId).isEqualTo("bet-1");
        verify(outboxRepository).save(any());
        verify(jackpotMetrics).recordBetAccepted();
    }

    @Test
    void produceBet_unknownJackpot_throwsException() {
        var command = new CreateBetCommand("bet-1", "user-1", "unknown", new BigDecimal("100.00"));
        when(jackpotRepository.findJackpotByJackpotId("unknown")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> betProducerService.produceBet(command))
                .isInstanceOf(JackpotNotFoundException.class);

        verify(betRepository, never()).saveAndFlush(any());
    }

    @Test
    void produceBet_duplicate_throwsException() {
        var command = new CreateBetCommand("bet-1", "user-1", "mega", new BigDecimal("100.00"));
        var jackpot = Jackpot.valueOf(
                "mega",
                new BigDecimal("1000.00"),
                new BigDecimal("1000.00"),
                ContributionType.FIXED,
                RewardType.FIXED
        );

        when(jackpotRepository.findJackpotByJackpotId("mega")).thenReturn(Optional.of(jackpot));
        when(betRepository.saveAndFlush(any(Bet.class)))
                .thenThrow(new org.springframework.dao.DataIntegrityViolationException("duplicate"));

        assertThatThrownBy(() -> betProducerService.produceBet(command))
                .isInstanceOf(BetAlreadyExistsException.class);

        verify(jackpotMetrics).recordDuplicateRejected();
    }
}
