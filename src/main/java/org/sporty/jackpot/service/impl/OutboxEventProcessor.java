package org.sporty.jackpot.service.impl;

import tools.jackson.databind.json.JsonMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sporty.jackpot.messaging.event.BetCreatedEvent;
import org.sporty.jackpot.model.OutboxEvent;
import org.sporty.jackpot.repository.OutboxRepository;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${jackpot.kafka.jackpot-bets-topic}")
    private String jackpotBetsTopic;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void publish(OutboxEvent event) throws Exception {
        var betCreatedEvent = jsonMapper.readValue(event.getPayload(), BetCreatedEvent.class);
        kafkaTemplate.send(jackpotBetsTopic, event.getAggregateId(), betCreatedEvent).get();
        event.markPublished();
        outboxRepository.save(event);
        log.info("Published outbox event id={} to topic={}", event.getId(), jackpotBetsTopic);
    }
}
