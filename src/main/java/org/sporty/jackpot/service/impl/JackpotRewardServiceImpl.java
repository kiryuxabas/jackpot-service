package org.sporty.jackpot.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sporty.jackpot.exception.BetNotFoundException;
import org.sporty.jackpot.exception.ContributionNotFoundException;
import org.sporty.jackpot.exception.JackpotNotFoundException;
import org.sporty.jackpot.model.JackpotReward;
import org.sporty.jackpot.observability.JackpotMetrics;
import org.sporty.jackpot.repository.BetRepository;
import org.sporty.jackpot.repository.JackpotContributionRepository;
import org.sporty.jackpot.repository.JackpotRepository;
import org.sporty.jackpot.repository.JackpotRewardRepository;
import org.sporty.jackpot.service.JackpotRewardService;
import org.sporty.jackpot.service.RewardStrategyFactory;
import org.sporty.jackpot.service.result.EvaluateBetResult;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Slf4j
@Service
@RequiredArgsConstructor
public class JackpotRewardServiceImpl implements JackpotRewardService {

    private final BetRepository betRepository;
    private final JackpotRepository jackpotRepository;
    private final JackpotContributionRepository jackpotContributionRepository;
    private final RewardStrategyFactory rewardStrategyFactory;
    private final JackpotRewardRepository jackpotRewardRepository;
    private final JackpotMetrics jackpotMetrics;

    @Override
    @Transactional
    public EvaluateBetResult evaluate(String betId) {
        log.debug("Evaluating reward for bet ID `{}`", betId);

        var bet = betRepository.findBetByBetId(betId)
                .orElseThrow(() -> new BetNotFoundException(betId));

        jackpotContributionRepository.findByBetId(betId)
                .orElseThrow(() -> new ContributionNotFoundException(betId));

        var jackpot = jackpotRepository.findJackpotByJackpotId(bet.getJackpotId())
                .orElseThrow(() -> new JackpotNotFoundException(bet.getJackpotId()));

        var rewardStrategy = rewardStrategyFactory.get(jackpot.getRewardType());
        if (!rewardStrategy.isWinner(jackpot.getCurrentPool())) {
            log.info("Bet ID `{}` did not win jackpot `{}`", betId, jackpot.getJackpotId());
            jackpotMetrics.recordRewardLoss();
            return new EvaluateBetResult(false, BigDecimal.ZERO);
        }

        var jackpotReward = JackpotReward.valueOf(
                bet.getBetId(),
                bet.getUserId(),
                jackpot.getJackpotId(),
                jackpot.getCurrentPool()
        );
        jackpotRewardRepository.save(jackpotReward);
        var rewardAmount = jackpot.payout();
        jackpotRepository.save(jackpot);
        jackpotMetrics.recordRewardWin();
        log.info("Bet ID `{}` won jackpot `{}` with reward amount `{}`",
                betId, jackpot.getJackpotId(), rewardAmount);
        return new EvaluateBetResult(true, rewardAmount);
    }
}
