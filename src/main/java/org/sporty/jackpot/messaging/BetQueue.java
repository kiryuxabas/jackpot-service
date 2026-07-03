package org.sporty.jackpot.messaging;

import org.sporty.jackpot.messaging.event.BetCreatedEvent;

public interface BetQueue {

    String produce(BetCreatedEvent betCreatedEvent);
    boolean isAlreadyExist(BetCreatedEvent betCreatedEvent);
}
