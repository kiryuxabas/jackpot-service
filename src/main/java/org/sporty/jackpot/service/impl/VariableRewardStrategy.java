package org.sporty.jackpot.service.impl;

import org.sporty.jackpot.service.RewardStrategy;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Random;

@Component
public class VariableRewardStrategy implements RewardStrategy {

    private final Random random = new Random();

    @Override
    public boolean isWinner(BigDecimal currentPool) {
        double chance = Math.min(currentPool.doubleValue() / 100_000.0, 1.0);
        return random.nextDouble() < chance;
    }
}