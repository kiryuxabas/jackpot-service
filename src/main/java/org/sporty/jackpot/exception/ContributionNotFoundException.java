package org.sporty.jackpot.exception;

public class ContributionNotFoundException extends RuntimeException {

    public ContributionNotFoundException(String betId) {
        super("Contribution for bet ID `%s` not found".formatted(betId));
    }
}
