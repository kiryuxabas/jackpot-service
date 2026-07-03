package org.sporty.jackpot.unit.service;

import org.junit.jupiter.api.Test;
import org.sporty.jackpot.model.ContributionType;
import org.sporty.jackpot.model.Jackpot;
import org.sporty.jackpot.model.RewardType;
import org.sporty.jackpot.service.impl.VariableContributionStrategy;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

class VariableContributionStrategyTest {

    private final VariableContributionStrategy strategy = new VariableContributionStrategy();

    @Test
    void calculateContribution_atInitialPool_returnsInitialPercent() {
        var jackpot = variableJackpot(new BigDecimal("500.00"));

        var contribution = strategy.calculateContribution(new BigDecimal("100.00"), jackpot);

        assertThat(contribution).isEqualByComparingTo("10.00");
    }

    @Test
    void calculateContribution_atPoolLimit_returnsFinalPercent() {
        var jackpot = variableJackpot(new BigDecimal("10000.00"));

        var contribution = strategy.calculateContribution(new BigDecimal("100.00"), jackpot);

        assertThat(contribution).isEqualByComparingTo("3.00");
    }

    @Test
    void calculateContribution_atMidPool_linearlyInterpolatesPercent() {
        var jackpot = variableJackpot(new BigDecimal("5250.00"));

        var contribution = strategy.calculateContribution(new BigDecimal("100.00"), jackpot);

        assertThat(contribution).isEqualByComparingTo("6.50");
    }

    private Jackpot variableJackpot(BigDecimal currentPool) {
        return Jackpot.valueOf(
                "super",
                new BigDecimal("500.00"),
                currentPool,
                ContributionType.VARIABLE,
                RewardType.VARIABLE,
                new BigDecimal("0.05"),
                new BigDecimal("0.10"),
                new BigDecimal("0.03"),
                new BigDecimal("10000.00"),
                new BigDecimal("0.01"),
                new BigDecimal("100000.00")
        );
    }
}
