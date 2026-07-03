package org.sporty.jackpot.service.impl;

import org.sporty.jackpot.service.RewardStrategy;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Random;

@Component
public class FixedRewardStrategy implements RewardStrategy {

    private final Random random = new Random();

    @Override
    public boolean isWinner(BigDecimal currentPool) {
        return random.nextDouble() < 0.01;
    }
}