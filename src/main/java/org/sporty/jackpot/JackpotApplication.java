package org.sporty.jackpot;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@OpenAPIDefinition(
        info = @Info(
                title = "Jackpot Service",
                version = "1.0",
                description = "Test assignment for jackpot processing"
        ),
        servers = @Server(url = "http://localhost:8080", description = "Local environment")
)
public class JackpotApplication {

    public static void main(String[] args) {
        SpringApplication.run(JackpotApplication.class, args);
    }

}
