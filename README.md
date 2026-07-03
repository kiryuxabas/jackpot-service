![CI/CD](https://img.shields.io/github/actions/workflow/status/kiryuxabas/jackpot-service/ci.yml?branch=main)
![Coverage](https://img.shields.io/badge/line%20coverage-91%25-brightgreen)
![Java](https://img.shields.io/badge/Java-21-orange)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4.0.7-brightgreen)
![Maven](https://img.shields.io/badge/Maven-3.9.16-blue)
![License](https://img.shields.io/badge/license-MIT-green)
![Jackpot Ready](https://img.shields.io/badge/Jackpot-Ready-gold)
![Simply the best ;)](https://img.shields.io/badge/simply-the%20best%20%3B%29-orange)

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

Run tests:
```sh
make test
```

Run linter:
```sh
make lint
```

Build image only:
```sh
make build
```

OpenAPI Doc:
```sh
http://localhost:8080/swagger-ui/index.html
```

**Improvement Plan**

* Enhance the SDLC pipeline by integrating build, linting, Sonar analysis, automated testing, code coverage reporting, security scanning, and deployment stages.
* Deploy and maintain Grafana dashboards using Grafonnet.
* Implement SLO/SLI-based alerting with Alertmanager.
* Enable Maven concurrent test execution and update the test suite to ensure compatibility and reliability with parallel test runs.


## SLO/SLI
- SLO/SLI definitions follow Google SRE methodology. See [docs/SLO.md](docs/SLO.md) for user journeys, targets, PromQL queries, and error budget policy.
- Prometheus metrics: `http://localhost:8080/actuator/prometheus`

## Tests & coverage

```sh
./mvnw test
```

Report: `target/site/jacoco/index.html`
