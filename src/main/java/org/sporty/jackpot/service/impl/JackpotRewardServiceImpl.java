package org.sporty.jackpot.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sporty.jackpot.exception.BetNotFoundException;
import org.sporty.jackpot.exception.ContributionNotFoundException;
import org.sporty.jackpot.exception.JackpotNotFoundException;
import org.sporty.jackpot.model.BetEvaluation;
import org.sporty.jackpot.model.JackpotReward;
import org.sporty.jackpot.observability.JackpotMetrics;
import org.sporty.jackpot.repository.BetEvaluationRepository;
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
    private final BetEvaluationRepository betEvaluationRepository;
    private final JackpotMetrics jackpotMetrics;

    @Override
    @Transactional
    public EvaluateBetResult evaluate(String betId) {
        log.debug("Evaluating reward for bet ID `{}`", betId);

        var existingEvaluation = betEvaluationRepository.findByBetId(betId);
        if (existingEvaluation.isPresent()) {
            var evaluation = existingEvaluation.get();
            log.info("Bet ID `{}` already evaluated: winner={}, amount={}",
                    betId, evaluation.isWinner(), evaluation.getRewardAmount());
            return new EvaluateBetResult(evaluation.isWinner(), evaluation.getRewardAmount());
        }

        var bet = betRepository.findBetByBetId(betId)
                .orElseThrow(() -> new BetNotFoundException(betId));

        jackpotContributionRepository.findByBetId(betId)
                .orElseThrow(() -> new ContributionNotFoundException(betId));

        var jackpot = jackpotRepository.findJackpotByJackpotId(bet.getJackpotId())
                .orElseThrow(() -> new JackpotNotFoundException(bet.getJackpotId()));

        var rewardStrategy = rewardStrategyFactory.get(jackpot.getRewardType());
        if (!rewardStrategy.isWinner(jackpot)) {
            log.info("Bet ID `{}` did not win jackpot `{}`", betId, jackpot.getJackpotId());
            jackpotMetrics.recordRewardLoss();
            var result = new EvaluateBetResult(false, BigDecimal.ZERO);
            betEvaluationRepository.save(BetEvaluation.valueOf(betId, false, BigDecimal.ZERO));
            return result;
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
        betEvaluationRepository.save(BetEvaluation.valueOf(betId, true, rewardAmount));
        jackpotMetrics.recordRewardWin();
        log.info("Bet ID `{}` won jackpot `{}` with reward amount `{}`",
                betId, jackpot.getJackpotId(), rewardAmount);
        return new EvaluateBetResult(true, rewardAmount);
    }
}
