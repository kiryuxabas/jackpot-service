package org.sporty.jackpot.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "bet_evaluations")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BetEvaluation {

    public static BetEvaluation valueOf(String betId, boolean winner, BigDecimal rewardAmount) {
        return new BetEvaluation(betId, winner, rewardAmount);
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String betId;

    @Column(nullable = false)
    private boolean winner;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal rewardAmount;

    @Column(nullable = false, updatable = false)
    private Instant evaluatedAt;

    private BetEvaluation(String betId, boolean winner, BigDecimal rewardAmount) {
        this.betId = betId;
        this.winner = winner;
        this.rewardAmount = rewardAmount;
        this.evaluatedAt = Instant.now();
    }
}
