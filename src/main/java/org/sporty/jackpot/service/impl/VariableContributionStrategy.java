package org.sporty.jackpot.service.impl;

import org.sporty.jackpot.model.Jackpot;
import org.sporty.jackpot.service.ContributionStrategy;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

@Component
public class VariableContributionStrategy implements ContributionStrategy {

    @Override
    public BigDecimal calculateContribution(BigDecimal betAmount, Jackpot jackpot) {
        var percent = resolveContributionPercent(jackpot);
        return betAmount.multiply(percent);
    }

    private BigDecimal resolveContributionPercent(Jackpot jackpot) {
        var initialPercent = jackpot.getContributionPercentInitial();
        var finalPercent = jackpot.getContributionPercentFinal();
        var poolLimit = jackpot.getContributionPoolLimit();
        var currentPool = jackpot.getCurrentPool();
        var initialPool = jackpot.getInitialPool();

        if (currentPool.compareTo(poolLimit) >= 0) {
            return finalPercent;
        }

        var poolRange = poolLimit.subtract(initialPool);
        if (poolRange.compareTo(BigDecimal.ZERO) <= 0) {
            return finalPercent;
        }

        var progress = currentPool.subtract(initialPool)
                .divide(poolRange, MathContext.DECIMAL64);
        if (progress.compareTo(BigDecimal.ZERO) < 0) {
            progress = BigDecimal.ZERO;
        }
        if (progress.compareTo(BigDecimal.ONE) > 0) {
            progress = BigDecimal.ONE;
        }

        return initialPercent.subtract(
                progress.multiply(initialPercent.subtract(finalPercent)),
                MathContext.DECIMAL64
        ).setScale(6, RoundingMode.HALF_UP);
    }
}
