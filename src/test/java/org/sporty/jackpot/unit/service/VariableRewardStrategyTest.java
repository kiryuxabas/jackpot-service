package org.sporty.jackpot.unit.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.sporty.jackpot.model.ContributionType;
import org.sporty.jackpot.model.Jackpot;
import org.sporty.jackpot.model.RewardType;
import org.sporty.jackpot.service.ChanceSource;
import org.sporty.jackpot.service.impl.VariableRewardStrategy;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class VariableRewardStrategyTest {

    @Mock
    private ChanceSource chanceSource;

    @Test
    void isWinner_whenPoolAtGuaranteedWinSize_alwaysWins() {
        when(chanceSource.next()).thenReturn(new BigDecimal("0.99"));

        var strategy = new VariableRewardStrategy(chanceSource);

        assertThat(strategy.isWinner(variableJackpot(new BigDecimal("100000.00")))).isTrue();
    }

    @Test
    void isWinner_whenChanceBelowComputedProbability_returnsTrue() {
        when(chanceSource.next()).thenReturn(new BigDecimal("0.4"));

        var strategy = new VariableRewardStrategy(chanceSource);

        assertThat(strategy.isWinner(variableJackpot(new BigDecimal("50000.00")))).isTrue();
    }

    @Test
    void isWinner_whenChanceAboveComputedProbability_returnsFalse() {
        when(chanceSource.next()).thenReturn(new BigDecimal("0.6"));

        var strategy = new VariableRewardStrategy(chanceSource);

        assertThat(strategy.isWinner(variableJackpot(new BigDecimal("50000.00")))).isFalse();
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
