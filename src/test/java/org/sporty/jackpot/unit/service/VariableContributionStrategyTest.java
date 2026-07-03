package org.sporty.jackpot.unit.service;

import org.junit.jupiter.api.Test;
import org.sporty.jackpot.service.impl.VariableContributionStrategy;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

class VariableContributionStrategyTest {

    private final VariableContributionStrategy strategy = new VariableContributionStrategy();

    @Test
    void calculateContribution_whenPoolBelowThreshold_returnsTenPercent() {
        var contribution = strategy.calculateContribution(new BigDecimal("100.00"), new BigDecimal("9999.99"));

        assertThat(contribution).isEqualByComparingTo("10.00");
    }

    @Test
    void calculateContribution_whenPoolAtOrAboveThreshold_returnsThreePercent() {
        var contribution = strategy.calculateContribution(new BigDecimal("100.00"), new BigDecimal("10000.00"));

        assertThat(contribution).isEqualByComparingTo("3.00");
    }
}
