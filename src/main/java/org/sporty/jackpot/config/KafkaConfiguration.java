package org.sporty.jackpot.config;

import org.sporty.jackpot.exception.JackpotNotFoundException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.util.backoff.FixedBackOff;

@Configuration
public class KafkaConfiguration {

    @Bean
    DefaultErrorHandler kafkaErrorHandler() {
        var handler = new DefaultErrorHandler(
                new FixedBackOff(
                        1000L,
                        3L
                )
        );

        handler.addNotRetryableExceptions(
                JackpotNotFoundException.class
        );

        return handler;
    }
}