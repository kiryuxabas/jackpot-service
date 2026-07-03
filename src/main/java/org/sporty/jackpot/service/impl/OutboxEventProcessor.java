package org.sporty.jackpot.service.impl;

import tools.jackson.databind.json.JsonMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sporty.jackpot.messaging.event.BetCreatedEvent;
import org.sporty.jackpot.observability.JackpotMetrics;
import org.sporty.jackpot.repository.OutboxRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class OutboxEventProcessor {

    private final OutboxRepository outboxRepository;
    private final KafkaTemplate<Object, Object> kafkaTemplate;
    private final JsonMapper jsonMapper;
    private final JackpotMetrics jackpotMetrics;

    @Value("${jackpot.kafka.jackpot-bets-topic}")
    private String jackpotBetsTopic;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public boolean publishNextUnpublished() throws Exception {
        var events = outboxRepository.claimUnpublished(PageRequest.of(0, 1));
        if (events.isEmpty()) {
            return false;
        }

        var event = events.getFirst();
        var betCreatedEvent = jsonMapper.readValue(event.getPayload(), BetCreatedEvent.class);
        kafkaTemplate.send(jackpotBetsTopic, event.getAggregateId(), betCreatedEvent).get();
        event.markPublished();
        outboxRepository.save(event);
        jackpotMetrics.recordOutboxPublishSuccess();
        log.info("Published outbox event id={} to topic={}", event.getId(), jackpotBetsTopic);
        return true;
    }
}
