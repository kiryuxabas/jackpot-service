package org.sporty.jackpot.rest.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record CreateBetRequest(
        @Schema(example = "bet-1") @NotBlank String betId,
        @Schema(example = "user-42") @NotBlank String userId,
        @Schema(example = "mega") @NotBlank String jackpotId,
        @Schema(example = "100") @Min(1) Integer betAmount
) {
}