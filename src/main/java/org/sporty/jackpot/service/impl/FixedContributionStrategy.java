package org.sporty.jackpot.service.impl;

import org.sporty.jackpot.model.Jackpot;
import org.sporty.jackpot.service.ContributionStrategy;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class FixedContributionStrategy implements ContributionStrategy {

    @Override
    public BigDecimal calculateContribution(BigDecimal betAmount, Jackpot jackpot) {
        return betAmount.multiply(jackpot.getContributionPercent());
    }
}
