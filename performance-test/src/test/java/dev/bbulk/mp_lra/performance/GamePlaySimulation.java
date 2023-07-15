package dev.bbulk.mp_lra.performance;

import io.gatling.javaapi.core.CoreDsl;
import io.gatling.javaapi.core.ScenarioBuilder;
import io.gatling.javaapi.core.Simulation;
import io.gatling.javaapi.http.HttpProtocolBuilder;

import java.time.Duration;
import java.util.UUID;

import static io.gatling.javaapi.core.CoreDsl.StringBody;
import static io.gatling.javaapi.core.CoreDsl.constantUsersPerSec;
import static io.gatling.javaapi.http.HttpDsl.http;
import static io.gatling.javaapi.http.HttpDsl.status;

public class GamePlaySimulation extends Simulation {

    // Protocol
    HttpProtocolBuilder httpProtocolBuilder = http
            .baseUrl("http://localhost:8081")
            .acceptHeader("application/json");

    // Scenario
    ScenarioBuilder scenarioBuilder = CoreDsl.scenario("Playing lots of games!")
            .exec(http("playGameRequest")
                    .post("/play")
                    .header("Content-Type", "application/json")
                    .body(StringBody("""
                            {
                                "player": "%s"
                            }
                            """.formatted(UUID.randomUUID())))
                    .check(status().is(204))
            );

    // Simulation
    public GamePlaySimulation() {
        setUp(scenarioBuilder.injectOpen(constantUsersPerSec(25).during(Duration.ofSeconds(60))))
                .protocols(httpProtocolBuilder);
    }


}
