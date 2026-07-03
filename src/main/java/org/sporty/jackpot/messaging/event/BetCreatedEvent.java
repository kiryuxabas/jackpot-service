package org.sporty.jackpot.messaging.event;

public record BetCreatedEvent(String betId, String userId, String jackpotId, Integer betAmount) {
}
