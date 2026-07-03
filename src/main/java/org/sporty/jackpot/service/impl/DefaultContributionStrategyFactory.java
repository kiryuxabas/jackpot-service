package org.sporty.jackpot.service.impl;

import lombok.RequiredArgsConstructor;
import org.sporty.jackpot.model.ContributionType;
import org.sporty.jackpot.service.ContributionStrategy;
import org.sporty.jackpot.service.ContributionStrategyFactory;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class DefaultContributionStrategyFactory implements ContributionStrategyFactory {

    private final Map<ContributionType, ContributionStrategy> strategies;

    public ContributionStrategy get(ContributionType type) {
        return strategies.get(type);
    }
}