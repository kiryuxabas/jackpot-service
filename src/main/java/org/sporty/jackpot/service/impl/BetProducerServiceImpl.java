package org.sporty.jackpot.service.impl;

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
public class BetProducerServiceImpl implements BetProducerService {

    private final BetRepository betRepository;
    private final OutboxRepository outboxRepository;
    private final JsonMapper jsonMapper;

    @Override
    @Transactional
    public String produceBet(CreateBetCommand command) {
        var bet = Bet.valueOf(
                command.betId(),
                command.userId(),
                command.jackpotId(),
                command.betAmount()
        );

        try {
            var savedBet = betRepository.save(bet);
            var event = new BetCreatedEvent(
                    command.betId(),
                    command.userId(),
                    command.jackpotId(),
                    command.betAmount()
            );
            outboxRepository.save(toOutboxEvent(event));
            return savedBet.getBetId();
        } catch (DataIntegrityViolationException _) {
            throw new BetAlreadyExistsException(command.betId());
        }
    }

    private OutboxEvent toOutboxEvent(BetCreatedEvent event) {
        try {
            var payload = jsonMapper.writeValueAsString(event);
            return OutboxEvent.create(event.betId(), BetCreatedEvent.class.getSimpleName(), payload);
        } catch (JacksonException e) {
            throw new IllegalStateException("Failed to serialize BetCreatedEvent", e);
        }
    }
}
