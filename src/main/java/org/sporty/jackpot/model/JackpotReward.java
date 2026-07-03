package org.sporty.jackpot.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "rewards")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class JackpotReward {

    public static JackpotReward valueOf(String betId, String userId, String jackpotId, BigDecimal rewardAmount) {
        return new JackpotReward(betId, userId, jackpotId, rewardAmount);
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
    private BigDecimal rewardAmount;

    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    private JackpotReward(String betId, String userId, String jackpotId, BigDecimal rewardAmount) {
        this.betId = betId;
        this.userId = userId;
        this.jackpotId = jackpotId;
        this.rewardAmount = rewardAmount;
        this.createdAt = Instant.now();
    }
}
