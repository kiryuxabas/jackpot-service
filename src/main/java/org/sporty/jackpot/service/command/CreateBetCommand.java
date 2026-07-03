package org.sporty.jackpot.service.command;

import java.math.BigDecimal;

public record CreateBetCommand(String betId, String userId, String jackpotId, BigDecimal betAmount) {
}