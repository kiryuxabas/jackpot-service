package org.sporty.jackpot.service.impl;

import org.sporty.jackpot.service.ContributionStrategy;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class VariableContributionStrategy implements ContributionStrategy {

    @Override
    public BigDecimal calculateContribution(BigDecimal betAmount, BigDecimal currentPool) {
        double percent = currentPool.doubleValue() < 10_000
                        ? 0.10
                        : 0.03;

        return betAmount.multiply(BigDecimal.valueOf(percent));
    }
}