package org.sporty.jackpot.service.impl;

import lombok.RequiredArgsConstructor;
import org.sporty.jackpot.service.ChanceSource;
import org.sporty.jackpot.service.RewardStrategy;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
public class FixedRewardStrategy implements RewardStrategy {

    private final ChanceSource chanceSource;

    @Override
    public boolean isWinner(BigDecimal currentPool) {
        return chanceSource.next() < 0.01;
    }
}
