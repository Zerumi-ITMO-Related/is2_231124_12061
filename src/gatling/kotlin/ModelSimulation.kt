package com.example.hackathon_becoder_backend.gatling

import io.gatling.javaapi.core.CoreDsl
import io.gatling.javaapi.core.Simulation
import io.gatling.javaapi.http.HttpDsl
import java.time.Duration
import kotlin.random.Random

class ModelSimulation : Simulation() {
    private val httpProtocol = HttpDsl.http.baseUrl("http://localhost:8080").contentTypeHeader("application/json")

    private val getTransactionsByLegalEntityId = CoreDsl.exec(
        HttpDsl.http("getting models")["/api/v1/model"].check(
            HttpDsl.status().`is`(200)
        )
    )

    private val randomCord: Int
        get() = Random.nextInt(605)

    private val authentication = CoreDsl.exec(
        HttpDsl.http("Authenticate").post("/api/v1/login").body(
            CoreDsl.StringBody(
                """{
  "login": "newUser_2",
  "password": "newUser_"
}"""
            )
        ).check(CoreDsl.jmesPath("token").saveAs("token"))
    )

    private val postModels = CoreDsl.exec(
        HttpDsl.http("creating models").post("/api/v1/model").header(
            "Authorization", "#{token}" // pass here token
        ).body(
            CoreDsl.StringBody(
                "{\n" +
                        "    \"name\": \"123\",\n" +
                        "    \"coordinates\": {\n" +
                        "        \"x\": ${randomCord},\n" +
                        "        \"y\": ${randomCord}\n" +
                        "    },\n" +
                        "    \"realHero\": true,\n" +
                        "    \"hasToothpick\": true,\n" +
                        "    \"car\": {\n" +
                        "        \"name\": \"143 \"\n" +
                        "    },\n" +
                        "    \"mood\": \"SADNESS\",\n" +
                        "    \"impactSpeed\": 432,\n" +
                        "    \"minutesOfWaiting\": 234,\n" +
                        "    \"weaponType\": \"AXE\"\n" +
                        "}"
            )
        ).check(
            HttpDsl.status().`in`(201),
            CoreDsl.jmesPath("id").saveAs("modelId")
        )
    )

    private val putModels = CoreDsl.exec(
        HttpDsl.http("updating models").put("/api/v1/model/#{modelId}").header(
            "Authorization", "#{token}"
        ).body(
            CoreDsl.StringBody(
                "{\n" +
                        "    \"name\": \"updated\",\n" +
                        "    \"coordinates\": {\n" +
                        "        \"x\": 3,\n" +
                        "        \"y\": 124\n" +
                        "    },\n" +
                        "    \"realHero\": true,\n" +
                        "    \"hasToothpick\": null,\n" +
                        "    \"car\": {\n" +
                        "        \"name\": \"143 \"\n" +
                        "    },\n" +
                        "    \"mood\": \"SADNESS\",\n" +
                        "    \"impactSpeed\": 432,\n" +
                        "    \"minutesOfWaiting\": 234,\n" +
                        "    \"weaponType\": \"AXE\"\n" +
                        "}"
            )
        ).check(
            HttpDsl.status().`is`(200)
        )
    )

    private val deleteModel = CoreDsl.exec(
        HttpDsl.http("deleting model").delete("/api/v1/model/#{modelId}")
            .header("Authorization", "#{token}")
            .check(HttpDsl.status().`is`(204))
    )

    private val crudTransaction = CoreDsl.scenario("crud models")
        .exec(authentication)
        .exec(postModels)
        .exec(putModels)
        .exec(deleteModel)

    init {
        setUp(
            crudTransaction.injectOpen(
                CoreDsl.constantUsersPerSec(1.0).during(Duration.ofSeconds(15))
            )
        ).protocols(httpProtocol)
    }
}
