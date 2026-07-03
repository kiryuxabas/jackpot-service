package org.sporty.jackpot.repository;

import org.sporty.jackpot.model.Bet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BetRepository extends JpaRepository<Bet, Long> {

    Optional<Bet> findBetByBetId(String betId);
}
