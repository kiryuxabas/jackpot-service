package org.sporty.jackpot.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sporty.jackpot.rest.dto.EvaluateBetResponse;
import org.sporty.jackpot.rest.dto.CreateBetRequest;
import org.sporty.jackpot.rest.dto.CreateBetResponse;
import org.sporty.jackpot.service.BetProducerService;
import org.sporty.jackpot.service.JackpotRewardService;
import org.sporty.jackpot.service.command.CreateBetCommand;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
public class JackpotEndpoint {

    private final BetProducerService betProducerService;
    private final JackpotRewardService jackpotRewardService;

    @PostMapping("/api/jackpot/bet")
    @Operation(summary = "Create bet", description = "Create a new bet and publish it to Kafka")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Bet successfully accepted"),
            @ApiResponse(responseCode = "400", description = "Invalid request"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public CreateBetResponse createBet(@Valid @RequestBody CreateBetRequest createBetRequest) {
        log.debug("Create bet request: betId={}, userId={}, jackpotId={}, amount={}",
                createBetRequest.betId(), createBetRequest.userId(),
                createBetRequest.jackpotId(), createBetRequest.betAmount());

        var cmd = new CreateBetCommand(
                createBetRequest.betId(),
                createBetRequest.userId(),
                createBetRequest.jackpotId(),
                createBetRequest.betAmount()
        );

        var betId = betProducerService.produceBet(cmd);
        log.info("Bet ID `{}` accepted via API", betId);
        return new CreateBetResponse(betId, "SUCCESS");
    }

    @PostMapping("/api/jackpot/bet/{betId}")
    public EvaluateBetResponse evaluateJackpotByBetId(@PathVariable String betId) {
        log.debug("Evaluate reward request for bet ID `{}`", betId);
        var result = jackpotRewardService.evaluate(betId);
        log.info("Reward evaluation for bet ID `{}`: winner={}, amount={}",
                betId, result.winner(), result.rewardAmount());
        return new EvaluateBetResponse(result.winner(), result.rewardAmount());
    }


}
