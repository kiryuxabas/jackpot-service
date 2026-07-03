package org.sporty.jackpot.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "bets")
@Getter
@Setter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Bet {

    public static Bet valueOf(String betId, String userId, String jackpotId, Integer betAmount) {
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
    private Integer betAmount;

    private Bet(String betId, String userId, String jackpotId, Integer betAmount) {
        this.betId = betId;
        this.userId = userId;
        this.jackpotId = jackpotId;
        this.betAmount = betAmount;
    }
}
