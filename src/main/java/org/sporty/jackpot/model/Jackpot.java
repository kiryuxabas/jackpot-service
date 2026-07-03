package org.sporty.jackpot.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "jackpots")
@Getter
@Setter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Jackpot {

    public static Jackpot valueOf(
            String jackpotId,
            BigDecimal initialPool,
            BigDecimal currentPool,
            ContributionType contributionType,
            RewardType rewardType,
            BigDecimal contributionPercent,
            BigDecimal contributionPercentInitial,
            BigDecimal contributionPercentFinal,
            BigDecimal contributionPoolLimit,
            BigDecimal rewardChancePercent,
            BigDecimal rewardChancePoolLimit
    ) {
        return new Jackpot(
                jackpotId,
                initialPool,
                currentPool,
                contributionType,
                rewardType,
                contributionPercent,
                contributionPercentInitial,
                contributionPercentFinal,
                contributionPoolLimit,
                rewardChancePercent,
                rewardChancePoolLimit
        );
    }

    public void addContribution(BigDecimal contribution) {
        this.currentPool = this.currentPool.add(contribution);
    }

    public BigDecimal payout() {
        var reward = currentPool;
        this.currentPool = initialPool;
        return reward;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String jackpotId;

    @Column(nullable = false)
    private BigDecimal initialPool;

    @Column(nullable = false)
    private BigDecimal currentPool;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ContributionType contributionType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RewardType rewardType;

    @Column(nullable = false, precision = 19, scale = 6)
    private BigDecimal contributionPercent;

    @Column(nullable = false, precision = 19, scale = 6)
    private BigDecimal contributionPercentInitial;

    @Column(nullable = false, precision = 19, scale = 6)
    private BigDecimal contributionPercentFinal;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal contributionPoolLimit;

    @Column(nullable = false, precision = 19, scale = 6)
    private BigDecimal rewardChancePercent;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal rewardChancePoolLimit;

    private Jackpot(
            String jackpotId,
            BigDecimal initialPool,
            BigDecimal currentPool,
            ContributionType contributionType,
            RewardType rewardType,
            BigDecimal contributionPercent,
            BigDecimal contributionPercentInitial,
            BigDecimal contributionPercentFinal,
            BigDecimal contributionPoolLimit,
            BigDecimal rewardChancePercent,
            BigDecimal rewardChancePoolLimit
    ) {
        this.jackpotId = jackpotId;
        this.initialPool = initialPool;
        this.currentPool = currentPool;
        this.contributionType = contributionType;
        this.rewardType = rewardType;
        this.contributionPercent = contributionPercent;
        this.contributionPercentInitial = contributionPercentInitial;
        this.contributionPercentFinal = contributionPercentFinal;
        this.contributionPoolLimit = contributionPoolLimit;
        this.rewardChancePercent = rewardChancePercent;
        this.rewardChancePoolLimit = rewardChancePoolLimit;
    }
}
