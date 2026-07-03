package org.sporty.jackpot.unit.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.sporty.jackpot.exception.BetNotFoundException;
import org.sporty.jackpot.exception.ContributionNotFoundException;
import org.sporty.jackpot.model.Bet;
import org.sporty.jackpot.model.ContributionType;
import org.sporty.jackpot.model.Jackpot;
import org.sporty.jackpot.model.JackpotContribution;
import org.sporty.jackpot.model.RewardType;
import org.sporty.jackpot.observability.JackpotMetrics;
import org.sporty.jackpot.repository.BetRepository;
import org.sporty.jackpot.repository.JackpotContributionRepository;
import org.sporty.jackpot.repository.JackpotRepository;
import org.sporty.jackpot.repository.JackpotRewardRepository;
import org.sporty.jackpot.service.RewardStrategy;
import org.sporty.jackpot.service.RewardStrategyFactory;
import org.sporty.jackpot.service.impl.JackpotRewardServiceImpl;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JackpotRewardServiceImplTest {

    @Mock
    private BetRepository betRepository;
    @Mock
    private JackpotRepository jackpotRepository;
    @Mock
    private JackpotContributionRepository jackpotContributionRepository;
    @Mock
    private RewardStrategyFactory rewardStrategyFactory;
    @Mock
    private JackpotRewardRepository jackpotRewardRepository;
    @Mock
    private JackpotMetrics jackpotMetrics;
    @Mock
    private RewardStrategy rewardStrategy;

    @InjectMocks
    private JackpotRewardServiceImpl jackpotRewardService;

    @Test
    void evaluate_betNotFound_throwsException() {
        when(betRepository.findBetByBetId("missing")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> jackpotRewardService.evaluate("missing"))
                .isInstanceOf(BetNotFoundException.class);
    }

    @Test
    void evaluate_contributionNotFound_throwsException() {
        var bet = Bet.valueOf("bet-1", "user-1", "mega", new BigDecimal("100.00"));
        when(betRepository.findBetByBetId("bet-1")).thenReturn(Optional.of(bet));
        when(jackpotContributionRepository.findByBetId("bet-1")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> jackpotRewardService.evaluate("bet-1"))
                .isInstanceOf(ContributionNotFoundException.class);
    }

    @Test
    void evaluate_loss_returnsZeroReward() {
        stubEvaluateContext(false);

        var result = jackpotRewardService.evaluate("bet-1");

        assertThat(result.winner()).isFalse();
        assertThat(result.rewardAmount()).isEqualByComparingTo(BigDecimal.ZERO);
        verify(jackpotMetrics).recordRewardLoss();
        verify(jackpotRewardRepository, never()).save(any());
    }

    @Test
    void evaluate_win_persistsRewardAndResetsJackpot() {
        var jackpot = stubEvaluateContext(true);

        var result = jackpotRewardService.evaluate("bet-1");

        assertThat(result.winner()).isTrue();
        assertThat(result.rewardAmount()).isEqualByComparingTo("1500.00");
        assertThat(jackpot.getCurrentPool()).isEqualByComparingTo("1000.00");
        verify(jackpotRewardRepository).save(any());
        verify(jackpotRepository).save(jackpot);
        verify(jackpotMetrics).recordRewardWin();
    }

    private Jackpot stubEvaluateContext(boolean winner) {
        var bet = Bet.valueOf("bet-1", "user-1", "mega", new BigDecimal("100.00"));
        var contribution = JackpotContribution.valueOf(
                "bet-1", "user-1", "mega",
                new BigDecimal("100.00"),
                new BigDecimal("5.00"),
                new BigDecimal("1000.00")
        );
        var jackpot = Jackpot.valueOf(
                "mega",
                new BigDecimal("1000.00"),
                new BigDecimal("1500.00"),
                ContributionType.FIXED,
                RewardType.FIXED
        );

        when(betRepository.findBetByBetId("bet-1")).thenReturn(Optional.of(bet));
        when(jackpotContributionRepository.findByBetId("bet-1")).thenReturn(Optional.of(contribution));
        when(jackpotRepository.findJackpotByJackpotId("mega")).thenReturn(Optional.of(jackpot));
        when(rewardStrategyFactory.get(RewardType.FIXED)).thenReturn(rewardStrategy);
        when(rewardStrategy.isWinner(jackpot.getCurrentPool())).thenReturn(winner);

        return jackpot;
    }
}
