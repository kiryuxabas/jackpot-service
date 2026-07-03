package org.sporty.jackpot.service.result;

import java.math.BigDecimal;

public record EvaluateBetResult(boolean winner, BigDecimal rewardAmount) {
}
