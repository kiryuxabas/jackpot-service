package org.sporty.jackpot.config;

import org.sporty.jackpot.model.ContributionType;
import org.sporty.jackpot.model.RewardType;
import org.sporty.jackpot.service.ChanceSource;
import org.sporty.jackpot.service.ContributionStrategy;
import org.sporty.jackpot.service.RewardStrategy;
import org.sporty.jackpot.service.impl.FixedContributionStrategy;
import org.sporty.jackpot.service.impl.FixedRewardStrategy;
import org.sporty.jackpot.service.impl.VariableContributionStrategy;
import org.sporty.jackpot.service.impl.VariableRewardStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

@Configuration
public class JackpotStrategyConfiguration {

    @Bean
    ChanceSource chanceSource() {
        return () -> BigDecimal.valueOf(ThreadLocalRandom.current().nextDouble());
    }

    @Bean
    Map<RewardType, RewardStrategy> rewardStrategies(
            FixedRewardStrategy fixedRewardStrategy,
            VariableRewardStrategy variableRewardStrategy
    ) {
        return Map.of(
                RewardType.FIXED, fixedRewardStrategy,
                RewardType.VARIABLE, variableRewardStrategy,
                RewardType
        );
    }

    @Bean
    Map<ContributionType, ContributionStrategy> contributionStrategies(
            FixedContributionStrategy fixedContributionStrategy,
            VariableContributionStrategy variableContributionStrategy
    ) {
        return Map.of(
                ContributionType.FIXED, fixedContributionStrategy,
                ContributionType.VARIABLE, variableContributionStrategy
        );
    }
}
