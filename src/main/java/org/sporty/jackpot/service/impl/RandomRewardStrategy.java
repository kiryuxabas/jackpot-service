package org.sporty.jackpot.service.impl;

import lombok.RequiredArgsConstructor;
import org.sporty.jackpot.model.Jackpot;
import org.sporty.jackpot.service.RewardStrategy;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RandomRewardStrategy implements RewardStrategy {

    private

    @Override
    public boolean isWinner(Jackpot jackpot) {
        return Math;
    }
}
