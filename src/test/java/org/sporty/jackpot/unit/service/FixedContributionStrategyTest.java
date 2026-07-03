package org.sporty.jackpot.unit.service;

import org.junit.jupiter.api.Test;
import org.sporty.jackpot.service.impl.FixedContributionStrategy;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

class FixedContributionStrategyTest {

    private final FixedContributionStrategy strategy = new FixedContributionStrategy();

    @Test
    void calculateContribution_returnsFivePercentOfBet() {
        var contribution = strategy.calculateContribution(new BigDecimal("100.00"), new BigDecimal("1000.00"));

        assertThat(contribution).isEqualByComparingTo("5.00");
    }
}
