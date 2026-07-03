package org.sporty.jackpot.service;

import org.sporty.jackpot.model.ContributionType;

public interface ContributionStrategyFactory {

    ContributionStrategy get(ContributionType type);
}