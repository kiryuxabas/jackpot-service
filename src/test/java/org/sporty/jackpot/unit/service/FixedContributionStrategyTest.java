package org.sporty.jackpot.unit.service;

import org.junit.jupiter.api.Test;
import org.sporty.jackpot.model.ContributionType;
import org.sporty.jackpot.model.Jackpot;
import org.sporty.jackpot.model.RewardType;
import org.sporty.jackpot.service.impl.FixedContributionStrategy;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

class FixedContributionStrategyTest {

    private final FixedContributionStrategy strategy = new FixedContributionStrategy();

    @Test
    void calculateContribution_returnsConfiguredPercentOfBet() {
        var jackpot = jackpotWithPool(new BigDecimal("1000.00"), new BigDecimal("0.05"));

        var contribution = strategy.calculateContribution(new BigDecimal("100.00"), jackpot);

        assertThat(contribution).isEqualByComparingTo("5.00");
    }

    private Jackpot jackpotWithPool(BigDecimal currentPool, BigDecimal contributionPercent) {
        return Jackpot.valueOf(
                "mega",
                new BigDecimal("1000.00"),
                currentPool,
                ContributionType.FIXED,
                RewardType.FIXED,
                contributionPercent,
                new BigDecimal("0.10"),
                new BigDecimal("0.03"),
                new BigDecimal("10000.00"),
                new BigDecimal("0.01"),
                new BigDecimal("100000.00")
        );
    }
}
