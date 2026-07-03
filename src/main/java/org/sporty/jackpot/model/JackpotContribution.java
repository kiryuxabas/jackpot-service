package org.sporty.jackpot.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "jackpot_contributions")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class JackpotContribution {

    public static JackpotContribution valueOf(
            String betId,
            String userId,
            String jackpotId,
            BigDecimal betAmount,
            BigDecimal contributionAmount,
            BigDecimal currentJackpotAmount
    ) {
        return new JackpotContribution(
                betId,
                userId,
                jackpotId,
                betAmount,
                contributionAmount,
                currentJackpotAmount
        );
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

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal betAmount;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal contributionAmount;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal currentJackpotAmount;

    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    private JackpotContribution(
            String betId,
            String userId,
            String jackpotId,
            BigDecimal betAmount,
            BigDecimal contributionAmount,
            BigDecimal currentJackpotAmount
    ) {
        this.betId = betId;
        this.userId = userId;
        this.jackpotId = jackpotId;
        this.betAmount = betAmount;
        this.contributionAmount = contributionAmount;
        this.currentJackpotAmount = currentJackpotAmount;
        this.createdAt = Instant.now();
    }
}