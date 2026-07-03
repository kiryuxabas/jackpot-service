package org.sporty.jackpot.exception;

public class JackpotNotFoundException extends RuntimeException {

    public JackpotNotFoundException(String jackpotId) {
        super("Jackpot ID `%s` not found".formatted(jackpotId));
    }
}
