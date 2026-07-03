package org.sporty.jackpot.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.sporty.jackpot.rest.dto.EvaluateBetResponse;
import org.sporty.jackpot.rest.dto.CreateBetRequest;
import org.sporty.jackpot.rest.dto.CreateBetResponse;
import org.sporty.jackpot.service.BetProducerService;
import org.sporty.jackpot.service.JackpotRewardService;
import org.sporty.jackpot.service.command.CreateBetCommand;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
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
        var cmd = new CreateBetCommand(
                createBetRequest.betId(),
                createBetRequest.userId(),
                createBetRequest.jackpotId(),
                createBetRequest.betAmount()
        );

        var betId = betProducerService.produceBet(cmd);
        return new CreateBetResponse(betId, "SUCCESS");
    }

    @PostMapping("/api/jackpot/bet/{betId}")
    public EvaluateBetResponse evaluateJackpotByBetId(@PathVariable String betId) {
        var result = jackpotRewardService.evaluate(betId);
        return new EvaluateBetResponse(result.winner(), result.rewardAmount());
    }


}
