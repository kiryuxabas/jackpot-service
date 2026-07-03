package org.sporty.jackpot.messaging.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sporty.jackpot.messaging.BetQueue;
import org.sporty.jackpot.messaging.event.BetCreatedEvent;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Slf4j
@Component
@RequiredArgsConstructor
public class InMemoryBetQueue implements BetQueue {

    private final Set<BetCreatedEvent> queue = new HashSet<>();

    @Override
    public String produce(BetCreatedEvent event) {
        queue.add(event);
        return event.betId();
    }

    @Override
    public boolean isAlreadyExist(BetCreatedEvent event) {
        return queue.contains(event);
    }

}
