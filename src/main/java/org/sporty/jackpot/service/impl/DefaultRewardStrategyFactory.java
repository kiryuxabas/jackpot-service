package org.sporty.jackpot.service.impl;

import lombok.RequiredArgsConstructor;
import org.sporty.jackpot.model.RewardType;
import org.sporty.jackpot.service.RewardStrategy;
import org.sporty.jackpot.service.RewardStrategyFactory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class DefaultRewardStrategyFactory implements RewardStrategyFactory {

    private final Map<RewardType, RewardStrategy> strategies;
//    private final List<RewardStrategy> strategies;

    public DefaultRewardStrategyFactory(List<RewardStrategy> list) {
        this.strategies = list.stream().collect(
                Collectors.toMap(
                        RewardStrategy::geType,
                        Function.identity()
                ));
    }

    @Override
    public RewardStrategy get(RewardType type) {
//        return strategies.
        return strategies.get(type);  //O(n) => O(1)
    }
}
