package org.sporty.jackpot.repository;

import org.sporty.jackpot.model.JackpotContribution;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface JackpotContributionRepository extends JpaRepository<JackpotContribution, Long> {

    Optional<JackpotContribution> findByBetId(String betId);
}
