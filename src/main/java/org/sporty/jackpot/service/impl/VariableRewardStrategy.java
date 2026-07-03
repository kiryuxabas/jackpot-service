package org.sporty.jackpot.service.impl;

import org.sporty.jackpot.service.RewardStrategy;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.Random;

@Component
public class VariableRewardStrategy implements RewardStrategy {

    private final Random random = new Random();

    private static final BigDecimal GUARANTEED_WIN_POOL_SIZE = BigDecimal.valueOf(100_000.00);

    @Override
    public boolean isWinner(BigDecimal currentPool) {
        double chance = Math.min(currentPool.divide(GUARANTEED_WIN_POOL_SIZE, MathContext.DECIMAL64).doubleValue(), 1.0);
        return random.nextDouble() < chance;
    }
}