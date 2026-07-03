package org.sporty.jackpot.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sporty.jackpot.observability.JackpotMetrics;
import org.sporty.jackpot.service.OutboxPublisher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class OutboxPublisherImpl implements OutboxPublisher {

    private final OutboxEventProcessor outboxEventProcessor;
    private final JackpotMetrics jackpotMetrics;

    @Override
    @Scheduled(fixedDelayString = "${jackpot.outbox.poll-interval-ms}")
    public void publishOutbox() {
        var publishedCount = 0;

        while (true) {
            try {
                if (!outboxEventProcessor.publishNextUnpublished()) {
                    break;
                }
                publishedCount++;
            } catch (Exception e) {
                jackpotMetrics.recordOutboxPublishFailure();
                log.error("Failed to publish outbox event", e);
                break;
            }
        }

        if (publishedCount == 0) {
            log.debug("No unpublished outbox events");
        } else {
            log.debug("Published {} outbox event(s)", publishedCount);
        }
    }
}
