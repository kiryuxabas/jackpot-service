package org.sporty.jackpot.service.command;

public record CreateBetCommand(String betId, String userId, String jackpotId, Integer betAmount) {
}