package org.sporty.jackpot.service;

// H2 does not support Debezium, so I implemented the Outbox Pattern from scratch
public interface OutboxPublisher {

    void publishOutbox();
}