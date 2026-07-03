package org.sporty.jackpot.service.impl;

import org.sporty.jackpot.service.ContributionStrategy;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class FixedContributionStrategy implements ContributionStrategy {

    public BigDecimal calculateContribution(BigDecimal betAmount, BigDecimal currentPool) {
        return betAmount.multiply(BigDecimal.valueOf(0.05));
    }
}
