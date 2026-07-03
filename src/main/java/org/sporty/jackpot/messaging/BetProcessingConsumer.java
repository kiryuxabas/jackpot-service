package org.sporty.jackpot.messaging;

import org.sporty.jackpot.messaging.event.BetCreatedEvent;

public interface BetProcessingConsumer {

    void onBet(BetCreatedEvent event);
}
