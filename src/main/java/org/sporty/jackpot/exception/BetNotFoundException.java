package org.sporty.jackpot.exception;

public class BetNotFoundException extends RuntimeException {

    public BetNotFoundException(String betId) {
        super("Bet ID `{}` not found".formatted(betId));
    }
}
