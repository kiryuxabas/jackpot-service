package org.sporty.jackpot.observability;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
public class JackpotMetrics {

    private static final Duration CONTRIBUTION_FRESHNESS_SLO = Duration.ofSeconds(5);

    private final Counter betAccepted;
    private final Counter betDuplicateRejected;
    private final Counter contributionProcessedSuccess;
    private final Counter contributionProcessedError;
    private final Counter contributionWithinSlo;
    private final Counter outboxPublishSuccess;
    private final Counter outboxPublishFailure;
    private final Counter rewardEvaluatedWin;
    private final Counter rewardEvaluatedLoss;
    private final Timer contributionProcessingDuration;

    public JackpotMetrics(MeterRegistry registry) {
        this.betAccepted = Counter.builder("jackpot.bet.accepted")
                .description("Number of bets accepted")
                .register(registry);
        this.betDuplicateRejected = Counter.builder("jackpot.bet.duplicate_rejected")
                .description("Number of duplicate bet ID rejections")
                .register(registry);
        this.contributionProcessedSuccess = Counter.builder("jackpot.contribution.processed")
                .tag("result", "success")
                .description("Number of contributions successfully processed")
                .register(registry);
        this.contributionProcessedError = Counter.builder("jackpot.contribution.processed")
                .tag("result", "error")
                .description("Number of contribution processing failures")
                .register(registry);
        this.contributionWithinSlo = Counter.builder("jackpot.contribution.within_slo")
                .tag("window", "5s")
                .description("Contributions processed within freshness SLO window")
                .register(registry);
        this.outboxPublishSuccess = Counter.builder("jackpot.outbox.publish")
                .tag("result", "success")
                .description("Number of successful outbox publishes")
                .register(registry);
        this.outboxPublishFailure = Counter.builder("jackpot.outbox.publish")
                .tag("result", "failure")
                .description("Number of failed outbox publishes")
                .register(registry);
        this.rewardEvaluatedWin = Counter.builder("jackpot.reward.evaluated")
                .tag("outcome", "win")
                .description("Number of winning reward evaluations")
                .register(registry);
        this.rewardEvaluatedLoss = Counter.builder("jackpot.reward.evaluated")
                .tag("outcome", "loss")
                .description("Number of losing reward evaluations")
                .register(registry);
        this.contributionProcessingDuration = Timer.builder("jackpot.contribution.processing.duration")
                .description("Time from bet acceptance to contribution persistence")
                .publishPercentileHistogram()
                .register(registry);
    }

    public void recordBetAccepted() {
        betAccepted.increment();
    }

    public void recordDuplicateRejected() {
        betDuplicateRejected.increment();
    }

    public void recordContributionSuccess(Duration duration) {
        contributionProcessedSuccess.increment();
        contributionProcessingDuration.record(duration);
        if (!duration.minus(CONTRIBUTION_FRESHNESS_SLO).isPositive()) {
            contributionWithinSlo.increment();
        }
    }

    public void recordContributionError() {
        contributionProcessedError.increment();
    }

    public void recordOutboxPublishSuccess() {
        outboxPublishSuccess.increment();
    }

    public void recordOutboxPublishFailure() {
        outboxPublishFailure.increment();
    }

    public void recordRewardWin() {
        rewardEvaluatedWin.increment();
    }

    public void recordRewardLoss() {
        rewardEvaluatedLoss.increment();
    }
}
