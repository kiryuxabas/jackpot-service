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
        var jackpot = Jackpot.valueOf(
                "mega",
                new BigDecimal("1000.00"),
                new BigDecimal("1000.00"),
                ContributionType.FIXED,
                RewardType.FIXED
        );

        jackpot.addContribution(new BigDecimal("50.00"));

        assertThat(jackpot.getCurrentPool()).isEqualByComparingTo("1050.00");
        assertThat(jackpot.getCurrentPool(), comparesEqualTo(new BigDecimal("1050.00")));
    }

    @Test
    void payout_returnsCurrentPoolAndResetsToInitial() {
        var jackpot = Jackpot.valueOf(
                "mega",
                new BigDecimal("1000.00"),
                new BigDecimal("1500.00"),
                ContributionType.FIXED,
                RewardType.FIXED
        );

        var reward = jackpot.payout();

        assertThat(reward).isEqualByComparingTo("1500.00");
        assertThat(jackpot.getCurrentPool(), comparesEqualTo(new BigDecimal("1000.00")));
    }
}
