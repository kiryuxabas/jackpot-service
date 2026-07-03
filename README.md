![CI/CD](https://img.shields.io/github/actions/workflow/status/kiryuxabas/jackpot-service/ci.yml?branch=main)
![Java](https://img.shields.io/badge/Java-21-orange)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4.0.7-brightgreen)
![Maven](https://img.shields.io/badge/Maven-3.9.16-blue)
![License](https://img.shields.io/badge/license-MIT-green)
![Jackpot Ready](https://img.shields.io/badge/Jackpot-Ready-gold)

<img align="right" width="25%" src="./images/big-duke.png">

# Jackpot Service
Ladies and gentlemen, this is my second test assignment for a big tech company, and I have only 50% confidence that I understand what is being discussed. The remaining 50% is just confidence that I can convince you that everything is going according to plan :) I hope that at least the second half is correct! :)

This is Jackpot Service including the following features:
*   made with Layered Architecture in mind (controller -> service -> repository)
*   has a possibility
*   made relatively fast :) but with :heart: :)

Run service:
```sh
make run
```

Swagger Doc:
```sh
http://localhost:8080/swagger-ui/index.html
```

## SLO/SLI
- SLO/SLI definitions follow Google SRE methodology. See [docs/SLO.md](docs/SLO.md) for user journeys, targets, PromQL queries, and error budget policy.
- Prometheus metrics: `http://localhost:8080/actuator/prometheus`
