package org.sporty.jackpot.service;

import org.sporty.jackpot.model.Jackpot;

import java.math.BigDecimal;

public interface ContributionStrategy {

    BigDecimal calculateContribution(BigDecimal betAmount, Jackpot jackpot);
}
