package org.sporty.jackpot.rest.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record CreateBetResponse(
        @Schema(example = "bet-1") String betId,
        @Schema(example = "ACCEPTED") String status
) {
}
