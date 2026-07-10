package org.sporty.jackpot.repository;

import jakarta.persistence.LockModeType;
import jakarta.persistence.QueryHint;
import org.sporty.jackpot.model.OutboxEvent;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;

import java.util.List;

public interface OutboxRepository extends JpaRepository<OutboxEvent, Long> {

    @Query("SELECT e FROM OutboxEvent e WHERE e.publishedAt IS NULL ORDER BY e.createdAt ASC")
    List<OutboxEvent> findUnpublished();

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @QueryHints(@QueryHint(name = "jakarta.persistence.lock.timeout", value = "-2"))  // select for update skip locked
    @Query("SELECT e FROM OutboxEvent e WHERE e.publishedAt IS NULL ORDER BY e.createdAt ASC")
    List<OutboxEvent> claimUnpublished(Pageable pageable);
    // jdbc: select ... for update skip locked
}
