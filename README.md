# scenario-runner

Local test application for Kafka-based scenario checks. Scenario evaluation is a stub; check types will be added later.

## Stack

- Java 21
- Maven (multi-module)
- Spring Boot 3.5
- Docker Compose (Apache Kafka in KRaft mode)

## Topics

| Purpose | Default name |
| --- | --- |
| Input JSON messages | `scenario.input` |
| Check results | `scenario.result` |

## Run tests

```bash
./mvnw test
```

## Run locally with Docker Compose

```bash
docker compose up --build
```

Kafka is available on `localhost:9092`. The app listens on `localhost:8080` (`GET /actuator/health`).

Produce a sample message (from another terminal, with Kafka CLI on the broker container):

```bash
docker compose exec kafka /opt/kafka/bin/kafka-console-producer.sh \
  --bootstrap-server localhost:9092 \
  --topic scenario.input
```

Paste a JSON line, for example:

```json
{"hello":"world"}
```

Consume results:

```bash
docker compose exec kafka /opt/kafka/bin/kafka-console-consumer.sh \
  --bootstrap-server localhost:9092 \
  --topic scenario.result \
  --from-beginning
```

The stub result has `"status":"STUB"` and `"reason":"Scenario evaluation not implemented"`.

## Run the JAR against Compose Kafka only

```bash
docker compose up kafka -d
./mvnw -pl scenario-runner-app -am spring-boot:run
```

The app uses `localhost:9092` by default.
