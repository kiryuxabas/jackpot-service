package org.sporty.jackpot.repository;

import org.sporty.jackpot.model.Bet;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BetRepository extends JpaRepository<Bet, Long> {

}
