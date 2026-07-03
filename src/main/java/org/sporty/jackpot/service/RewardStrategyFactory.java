package org.sporty.jackpot.service;

import org.sporty.jackpot.model.RewardType;

public interface RewardStrategyFactory {

    RewardStrategy get(RewardType type);
}
