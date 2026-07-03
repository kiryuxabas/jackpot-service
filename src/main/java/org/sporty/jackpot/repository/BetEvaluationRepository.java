package org.sporty.jackpot.repository;

import org.sporty.jackpot.model.BetEvaluation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BetEvaluationRepository extends JpaRepository<BetEvaluation, Long> {

    Optional<BetEvaluation> findByBetId(String betId);
}
