package org.sporty.jackpot.messaging.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sporty.jackpot.exception.JackpotNotFoundException;
import org.sporty.jackpot.messaging.BetProcessingConsumer;
import org.sporty.jackpot.messaging.event.BetCreatedEvent;
import org.sporty.jackpot.model.JackpotContribution;
import org.sporty.jackpot.observability.JackpotMetrics;
import org.sporty.jackpot.repository.BetRepository;
import org.sporty.jackpot.repository.JackpotContributionRepository;
import org.sporty.jackpot.repository.JackpotRepository;
import org.sporty.jackpot.service.ContributionStrategyFactory;
import org.sporty.jackpot.service.JackpotRewardService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaBetProcessingConsumerImpl implements BetProcessingConsumer {

    private final BetRepository betRepository;
    private final JackpotRepository jackpotRepository;
    private final JackpotContributionRepository jackpotContributionRepository;
    private final ContributionStrategyFactory contributionStrategyFactory;
    private final JackpotRewardService jackpotRewardService;
    private final JackpotMetrics jackpotMetrics;

    @Transactional
    @KafkaListener(topics = "${jackpot.kafka.jackpot-bets-topic}")
    public void onBet(@Payload BetCreatedEvent event) {
        log.debug("Received bet ID `{}` for jackpot `{}`", event.betId(), event.jackpotId());

        try {
            var jackpot = jackpotRepository.findJackpotByJackpotId(event.jackpotId())
                    .orElseThrow(() -> new JackpotNotFoundException(event.jackpotId()));

            var contributionStrategy = contributionStrategyFactory.get(jackpot.getContributionType());
            var contributionValue = contributionStrategy.calculateContribution(event.betAmount(), jackpot);

            jackpot.addContribution(contributionValue);

            var jackpotContribution = JackpotContribution.valueOf(
                    event.betId(),
                    event.userId(),
                    event.jackpotId(),
                    event.betAmount(),
                    contributionValue,
                    jackpot.getCurrentPool()
            );
            jackpotContributionRepository.saveAndFlush(jackpotContribution);
            jackpotRepository.save(jackpot);

            var betCreatedAt = betRepository.findBetByBetId(event.betId())
                    .map(bet -> bet.getCreatedAt())
                    .orElse(Instant.now());
            var processingDuration = Duration.between(betCreatedAt, Instant.now());
            jackpotMetrics.recordContributionSuccess(processingDuration);

            log.info("Bet ID `{}` contributed `{}` to jackpot `{}`. Current pool `{}`",
                    event.betId(), contributionValue, jackpot.getJackpotId(), jackpot.getCurrentPool());

            var evaluation = jackpotRewardService.evaluate(event.betId());
            log.info("Bet ID `{}` auto-evaluated after contribution: winner={}, amount={}",
                    event.betId(), evaluation.winner(), evaluation.rewardAmount());
        } catch (Exception e) {
            jackpotMetrics.recordContributionError();
            log.error("Failed to process contribution for bet ID `{}`, jackpot `{}`",
                    event.betId(), event.jackpotId(), e);
            throw e;
        }
    }
}
