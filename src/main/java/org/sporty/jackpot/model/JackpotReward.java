package org.sporty.jackpot.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "rewards")
@Getter
@Setter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class JackpotReward {

    public static JackpotReward valueOf(String betId, String userId, String jackpotId, BigDecimal currentPool) {
        return new JackpotReward(betId, userId, jackpotId, currentPool);
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
    private BigDecimal currentPool;

    private JackpotReward(String betId, String userId, String jackpotId, BigDecimal currentPool) {
        this.betId = betId;
        this.userId = userId;
        this.jackpotId = jackpotId;
        this.currentPool = currentPool;
    }
}
