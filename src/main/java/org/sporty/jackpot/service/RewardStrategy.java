package org.sporty.jackpot.service;

import org.sporty.jackpot.model.Jackpot;

public interface RewardStrategy {

    boolean isWinner(Jackpot jackpot);
}
