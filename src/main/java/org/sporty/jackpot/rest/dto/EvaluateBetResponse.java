package org.sporty.jackpot.rest.dto;

import java.math.BigDecimal;

public record EvaluateBetResponse(boolean winner, BigDecimal rewardAmount) {
}
