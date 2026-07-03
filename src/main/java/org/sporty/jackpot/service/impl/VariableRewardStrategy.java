package org.sporty.jackpot.service.impl;

import lombok.RequiredArgsConstructor;
import org.sporty.jackpot.model.Jackpot;
import org.sporty.jackpot.service.ChanceSource;
import org.sporty.jackpot.service.RewardStrategy;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.MathContext;

@Component
@RequiredArgsConstructor
public class VariableRewardStrategy implements RewardStrategy {

    private final ChanceSource chanceSource;

    @Override
    public boolean isWinner(Jackpot jackpot) {
        var poolLimit = jackpot.getRewardChancePoolLimit();
        if (poolLimit.compareTo(BigDecimal.ZERO) <= 0) {
            return false;
        }

        var chance = jackpot.getCurrentPool()
                .divide(poolLimit, MathContext.DECIMAL64)
                .min(BigDecimal.ONE);
        return chanceSource.next().compareTo(chance) < 0;
    }
}
