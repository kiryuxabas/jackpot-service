package org.sporty.jackpot.service.impl;

import lombok.RequiredArgsConstructor;
import org.sporty.jackpot.service.ChanceSource;
import org.sporty.jackpot.service.RewardStrategy;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.MathContext;

@Component
@RequiredArgsConstructor
public class VariableRewardStrategy implements RewardStrategy {

    private static final BigDecimal GUARANTEED_WIN_POOL_SIZE = BigDecimal.valueOf(100_000.00);

    private final ChanceSource chanceSource;

    @Override
    public boolean isWinner(BigDecimal currentPool) {
        double chance = Math.min(
                currentPool.divide(GUARANTEED_WIN_POOL_SIZE, MathContext.DECIMAL64).doubleValue(),
                1.0
        );
        return chanceSource.next() < chance;
    }
}
