package org.sporty.jackpot.exception;

public class BetAlreadyExistsException extends RuntimeException {

    public BetAlreadyExistsException(String betId) {
        super("Bet ID `%s` already exists".formatted(betId));
    }
}
