package org.sporty.jackpot.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "bets")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Bet {

    public static Bet valueOf(String betId, String userId, String jackpotId, BigDecimal betAmount) {
        return new Bet(betId, userId, jackpotId, betAmount);
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String betId;

    @Column(nullable = false)
    private String userId;

    @Column(nullable = false)
    private String jackpotId;

    @Column(nullable = false)
    private BigDecimal betAmount;

    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    private Bet(String betId, String userId, String jackpotId, BigDecimal betAmount) {
        this.betId = betId;
        this.userId = userId;
        this.jackpotId = jackpotId;
        this.betAmount = betAmount;
        this.createdAt = Instant.now();
    }
}
