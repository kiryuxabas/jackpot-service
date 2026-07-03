package org.sporty.jackpot.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.sporty.jackpot.messaging.event.BetCreatedEvent;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class BetProcessingImpl {

    @KafkaListener(topics = "${jackpot.kafka.bet-created-topic}")
    void onBet(@Payload BetCreatedEvent event) {
        log.debug("Received bet ID `{}` for jackpot `{}`", event.betId(), event.jackpotId());

    }
}
