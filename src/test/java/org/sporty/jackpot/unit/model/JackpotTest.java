package org.sporty.jackpot.unit.model;

import org.junit.jupiter.api.Test;
import org.sporty.jackpot.model.ContributionType;
import org.sporty.jackpot.model.Jackpot;
import org.sporty.jackpot.model.RewardType;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.comparesEqualTo;

class JackpotTest {

    @Test
    void addContribution_increasesCurrentPool() {
        var jackpot = testJackpot(new BigDecimal("1000.00"));

        jackpot.addContribution(new BigDecimal("50.00"));

        assertThat(jackpot.getCurrentPool()).isEqualByComparingTo("1050.00");
        assertThat(jackpot.getCurrentPool(), comparesEqualTo(new BigDecimal("1050.00")));
    }

    @Test
    void payout_returnsCurrentPoolAndResetsToInitial() {
        var jackpot = testJackpot(new BigDecimal("1500.00"));

        var reward = jackpot.payout();

        assertThat(reward).isEqualByComparingTo("1500.00");
        assertThat(jackpot.getCurrentPool(), comparesEqualTo(new BigDecimal("1000.00")));
    }

    private Jackpot testJackpot(BigDecimal currentPool) {
        return Jackpot.valueOf(
                "mega",
                new BigDecimal("1000.00"),
                currentPool,
                ContributionType.FIXED,
                RewardType.FIXED,
                new BigDecimal("0.05"),
                new BigDecimal("0.10"),
                new BigDecimal("0.03"),
                new BigDecimal("10000.00"),
                new BigDecimal("0.01"),
                new BigDecimal("100000.00")
        );
    }
}
