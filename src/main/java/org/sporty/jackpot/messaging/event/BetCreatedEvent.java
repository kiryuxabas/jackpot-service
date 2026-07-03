package org.sporty.jackpot.messaging.event;

import java.math.BigDecimal;

public record BetCreatedEvent(String betId, String userId, String jackpotId, BigDecimal betAmount) {
}
