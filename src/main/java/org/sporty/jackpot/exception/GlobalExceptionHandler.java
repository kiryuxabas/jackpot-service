package org.sporty.jackpot.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BetAlreadyExistsException.class)
    public ProblemDetail handle(BetAlreadyExistsException e) {
        log.warn("Bet already exists: {}", e.getMessage());
        var problem = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        problem.setTitle("Bet already exists");
        problem.setDetail(e.getMessage());
        return problem;
    }

    @ExceptionHandler(BetNotFoundException.class)
    public ProblemDetail handle(BetNotFoundException e) {
        log.warn("Bet not found: {}", e.getMessage());
        var problem = ProblemDetail.forStatus(HttpStatus.NOT_FOUND);
        problem.setTitle("Bet not found");
        problem.setDetail(e.getMessage());
        return problem;
    }

    @ExceptionHandler(ContributionNotFoundException.class)
    public ProblemDetail handle(ContributionNotFoundException e) {
        log.warn("Contribution not found: {}", e.getMessage());
        var problem = ProblemDetail.forStatus(HttpStatus.NOT_FOUND);
        problem.setTitle("Contribution not found");
        problem.setDetail(e.getMessage());
        return problem;
    }

    @ExceptionHandler(JackpotNotFoundException.class)
    public ProblemDetail handle(JackpotNotFoundException e) {
        log.warn("Jackpot not found: {}", e.getMessage());
        var problem = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        problem.setTitle("Jackpot not found");
        problem.setDetail(e.getMessage());
        return problem;
    }
}
