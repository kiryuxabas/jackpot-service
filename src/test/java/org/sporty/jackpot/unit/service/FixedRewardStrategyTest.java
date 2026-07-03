package org.sporty.jackpot.unit.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
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
        when(chanceSource.next()).thenReturn(0.005);

        var strategy = new FixedRewardStrategy(chanceSource);

        assertThat(strategy.isWinner(new BigDecimal("1000.00"))).isTrue();
        assertThat(strategy.isWinner(new BigDecimal("1000.00")), is(true));
    }

    @Test
    void isWinner_whenChanceAboveThreshold_returnsFalse() {
        when(chanceSource.next()).thenReturn(0.5);

        var strategy = new FixedRewardStrategy(chanceSource);

        assertThat(strategy.isWinner(new BigDecimal("1000.00"))).isFalse();
    }
}
