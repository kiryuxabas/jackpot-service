package org.sporty.jackpot.service;

import java.math.BigDecimal;

public interface RewardStrategy {

    boolean isWinner(BigDecimal currentPool);
}