package org.sporty.jackpot.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sporty.jackpot.messaging.event.BetCreatedEvent;
import org.sporty.jackpot.model.OutboxEvent;
import org.sporty.jackpot.observability.JackpotMetrics;
import org.sporty.jackpot.repository.OutboxRepository;
import org.sporty.jackpot.service.OutboxPublisher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class OutboxPublisherImpl implements OutboxPublisher {

    private final OutboxRepository outboxRepository;
    private final OutboxEventProcessor outboxEventProcessor;
    private final JackpotMetrics jackpotMetrics;

    @Override
    @Scheduled(fixedDelayString = "${jackpot.outbox.poll-interval-ms}")
    public void publishOutbox() {
        var events = outboxRepository.findUnpublished();
        if (events.isEmpty()) {
            log.debug("No unpublished outbox events");
            return;
        }

        log.debug("Publishing {} unpublished outbox event(s)", events.size());

        for (var event : events) {
            try {
                outboxEventProcessor.publish(event);
            } catch (Exception e) {
                jackpotMetrics.recordOutboxPublishFailure();
                log.error("Failed to publish outbox event id={}, aggregateId={}", event.getId(), event.getAggregateId(), e);
            }
        }
    }
}
