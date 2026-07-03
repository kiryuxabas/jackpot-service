package org.sporty.jackpot.service;

import org.sporty.jackpot.service.command.CreateBetCommand;

public interface BetProducerService {

    String produceBet(CreateBetCommand command);
}
