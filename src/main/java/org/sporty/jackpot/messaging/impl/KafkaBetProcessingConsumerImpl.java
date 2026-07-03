package org.sporty.jackpot.messaging.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sporty.jackpot.exception.JackpotNotFoundException;
import org.sporty.jackpot.messaging.BetProcessingConsumer;
import org.sporty.jackpot.messaging.event.BetCreatedEvent;
import org.sporty.jackpot.model.JackpotContribution;
import org.sporty.jackpot.repository.JackpotContributionRepository;
import org.sporty.jackpot.repository.JackpotRepository;
import org.sporty.jackpot.service.ContributionStrategyFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaBetProcessingConsumerImpl implements BetProcessingConsumer {

    private final JackpotRepository jackpotRepository;
    private final JackpotContributionRepository jackpotContributionRepository;
    private final ContributionStrategyFactory contributionStrategyFactory;

    @Transactional
    @KafkaListener(topics = "${jackpot.kafka.bet-created-topic}")
    public void onBet(@Payload BetCreatedEvent event) {
        log.debug("Received bet ID `{}` for jackpot `{}`", event.betId(), event.jackpotId());

        var jackpot = jackpotRepository.findJackpotByJackpotId(event.jackpotId())
                .orElseThrow(() -> new JackpotNotFoundException(event.jackpotId()));

        var contributionStrategy = contributionStrategyFactory.get(jackpot.getContributionType());
        var contributionValue = contributionStrategy.calculateContribution(event.betAmount(), jackpot.getCurrentPool());

        var jackpotContribution = JackpotContribution.valueOf(
                event.betId(),
                event.userId(),
                event.jackpotId(),
                event.betAmount(),
                contributionValue,
                jackpot.getCurrentPool()
        );
        jackpotContributionRepository.saveAndFlush(jackpotContribution);

        jackpot.addContribution(contributionValue);
        jackpotRepository.save(jackpot);

        log.info("Bet ID `{}` contributed `{}` to jackpot `{}`. Current pool `{}`",
                event.betId(), contributionValue, jackpot.getJackpotId(), jackpot.getCurrentPool());
    }
}
