package org.sporty.jackpot.service;

import org.sporty.jackpot.model.Jackpot;
import org.sporty.jackpot.model.RewardType;

public interface RewardStrategy {

    boolean isWinner(Jackpot jackpot);
    RewardType geType();
}
