package org.sporty.jackpot.service.impl;

import org.sporty.jackpot.exception.JackpotNotFoundException;
import org.sporty.jackpot.observability.JackpotMetrics;
import org.sporty.jackpot.repository.JackpotRepository;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.json.JsonMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sporty.jackpot.exception.BetAlreadyExistsException;
import org.sporty.jackpot.messaging.event.BetCreatedEvent;
import org.sporty.jackpot.model.Bet;
import org.sporty.jackpot.model.OutboxEvent;
import org.sporty.jackpot.repository.BetRepository;
import org.sporty.jackpot.repository.OutboxRepository;
import org.sporty.jackpot.service.BetProducerService;
import org.sporty.jackpot.service.command.CreateBetCommand;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class H2BetProducerServiceImpl implements BetProducerService {

    private final BetRepository betRepository;
    private final OutboxRepository outboxRepository;
    private final JsonMapper jsonMapper;
    private final JackpotRepository jackpotRepository;
    private final JackpotMetrics jackpotMetrics;

    @Override
    @Transactional
    public String produceBet(CreateBetCommand command) {
        var bet = Bet.valueOf(
                command.betId(),
                command.userId(),
                command.jackpotId(),
                command.betAmount()
        );

        if (jackpotRepository.findJackpotByJackpotId(bet.getJackpotId()).isEmpty()) {
            throw new JackpotNotFoundException(bet.getJackpotId());
        }

        try {
            var savedBet = betRepository.saveAndFlush(bet);
            var event = new BetCreatedEvent(
                    command.betId(),
                    command.userId(),
                    command.jackpotId(),
                    command.betAmount()
            );
            outboxRepository.save(toOutboxEvent(event));
            jackpotMetrics.recordBetAccepted();
            log.info("Bet ID `{}` saved for jackpot `{}` with amount `{}`, outbox event queued",
                    savedBet.getBetId(), savedBet.getJackpotId(), savedBet.getBetAmount());
            return savedBet.getBetId();
        } catch (DataIntegrityViolationException ex) {
            log.warn("Duplicate bet ID `{}` detected", command.betId());
            jackpotMetrics.recordDuplicateRejected();
            throw new BetAlreadyExistsException(command.betId());
        }
    }

    private OutboxEvent toOutboxEvent(BetCreatedEvent event) {
        try {
            var payload = jsonMapper.writeValueAsString(event);
            return OutboxEvent.create(event.betId(), BetCreatedEvent.class.getSimpleName(), payload);
        } catch (JacksonException e) {
            log.error("Failed to serialize BetCreatedEvent for bet ID `{}`", event.betId(), e);
            throw new IllegalStateException("Failed to serialize BetCreatedEvent", e);
        }
    }
}
