package org.sporty.jackpot.service;

import org.sporty.jackpot.service.result.EvaluateBetResult;

public interface JackpotRewardService {

    EvaluateBetResult evaluate(String betId);

}
