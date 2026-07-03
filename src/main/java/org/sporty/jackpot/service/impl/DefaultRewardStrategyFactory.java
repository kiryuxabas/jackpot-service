package org.sporty.jackpot.service.impl;

import lombok.RequiredArgsConstructor;
import org.sporty.jackpot.model.RewardType;
import org.sporty.jackpot.service.RewardStrategy;
import org.sporty.jackpot.service.RewardStrategyFactory;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class DefaultRewardStrategyFactory implements RewardStrategyFactory {

    private final Map<RewardType, RewardStrategy> strategies;

    @Override
    public RewardStrategy get(RewardType type) {
        return strategies.get(type);
    }
}
