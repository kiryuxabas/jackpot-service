package org.sporty.jackpot.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sporty.jackpot.exception.BetAlreadyExistsException;
import org.sporty.jackpot.model.Bet;
import org.sporty.jackpot.repository.BetRepository;
import org.sporty.jackpot.service.BetProducerService;
import org.sporty.jackpot.service.command.CreateBetCommand;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class BetProducerServiceImpl implements BetProducerService {

    private final BetRepository betRepository;

    public String produceBet(CreateBetCommand command) {
        var bet = Bet.valueOf(
                command.betId(),
                command.userId(),
                command.jackpotId(),
                command.betAmount()
        );

        try {
            return betRepository.save(bet).getBetId();
        } catch (DataIntegrityViolationException _) {
            throw new BetAlreadyExistsException(command.betId());
        }
    }
}
