package org.sporty.jackpot.repository;

import jakarta.persistence.LockModeType;
import org.sporty.jackpot.model.Jackpot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

import java.util.Optional;

public interface JackpotRepository extends JpaRepository<Jackpot, Long> {

    // use pessimistic instead of optimistic, because we do not need constant retries on concurrent requests
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<Jackpot> findJackpotByJackpotId(String jackpotId);
}
