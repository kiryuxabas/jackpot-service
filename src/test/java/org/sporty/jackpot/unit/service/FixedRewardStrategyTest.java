package org.sporty.jackpot.unit.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.sporty.jackpot.model.ContributionType;
import org.sporty.jackpot.model.Jackpot;
import org.sporty.jackpot.model.RewardType;
import org.sporty.jackpot.service.ChanceSource;
import org.sporty.jackpot.service.impl.FixedRewardStrategy;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FixedRewardStrategyTest {

    @Mock
    private ChanceSource chanceSource;

    @Test
    void isWinner_whenChanceBelowThreshold_returnsTrue() {
        when(chanceSource.next()).thenReturn(new BigDecimal("0.005"));

        var strategy = new FixedRewardStrategy(chanceSource);

        assertThat(strategy.isWinner(fixedJackpot())).isTrue();
        assertThat(strategy.isWinner(fixedJackpot()), is(true));
    }

    @Test
    void isWinner_whenChanceAboveThreshold_returnsFalse() {
        when(chanceSource.next()).thenReturn(new BigDecimal("0.5"));

        var strategy = new FixedRewardStrategy(chanceSource);

        assertThat(strategy.isWinner(fixedJackpot())).isFalse();
    }

    private Jackpot fixedJackpot() {
        return Jackpot.valueOf(
                "mega",
                new BigDecimal("1000.00"),
                new BigDecimal("1000.00"),
                ContributionType.FIXED,
                RewardType.FIXED,
                new BigDecimal("0.05"),
                new BigDecimal("0.10"),
                new BigDecimal("0.03"),
                new BigDecimal("10000.00"),
                new BigDecimal("0.01"),
                new BigDecimal("100000.00")
        );
    }
}
