package org.sporty.jackpot.service.impl;

import lombok.RequiredArgsConstructor;
import org.sporty.jackpot.model.Jackpot;
import org.sporty.jackpot.service.ChanceSource;
import org.sporty.jackpot.service.RewardStrategy;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FixedRewardStrategy implements RewardStrategy {

    private final ChanceSource chanceSource;

    @Override
    public boolean isWinner(Jackpot jackpot) {
        return chanceSource.next().compareTo(jackpot.getRewardChancePercent()) < 0;
    }
}
