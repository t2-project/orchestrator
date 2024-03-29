# Orchestrator Service

This service is part of the T2-Project.
It orchestrates the saga.

See the [Documentation](https://t2-documentation.readthedocs.io/en/latest/microservices/arch.html#the-saga) for more details on the saga.

## Build and Run

Refer to the [Documentation](https://t2-documentation.readthedocs.io/en/latest/microservices/deploy.html) on how to build, run or deploy the T2-Project services.

## HTTP Endpoints

* `/order` POST here to start a saga

## Usage

With this, you place an order for the user "foo", with the given payment details and a total cost of 42.0 money.
If the orchestrator service successfully started a saga instance to handle the order, it returns the id of the saga instance.

```sh
curl -i -X POST -H "Content-Type:application/json" -d '{"cardNumber":"num","cardOwner":"own","checksum":"sum", "sessionId":"foo", "total" : 42.0}' http://orchestrator/order
```

```plain
000001796223c175-ee79d7ce3aa50000
```

## Application Properties

| property | read from env var |
| -------- | ----------------- |
| spring.datasource.url | SPRING_DATASOURCE_URL |
| spring.datasource.username | SPRING_DATASOURCE_USERNAME |
| spring.datasource.password | SPRING_DATASOURCE_PASSWORD |
| spring.datasource.driver-class-name | SPRING_DATASOURCE_DRIVER_CLASS_NAME |
| eventuatelocal.kafka.bootstrap.servers | EVENTUATELOCAL_KAFKA_BOOTSTRAP_SERVERS |
| eventuatelocal.zookeeper.connection.string | EVENTUATELOCAL_ZOOKEEPER_CONNECTION_STRING |
| t2.gmt-logging.enabled | T2_GMT_LOGGING_ENABLED |

Documentation of the CDC properties: [eventuate tram cdc](https://eventuate.io/docs/manual/eventuate-tram/latest/getting-started-eventuate-tram.html)

The property `t2.gmt-logging.enabled` is used to enable/disable custom logging for energy measurements with the [Green Metrics Tool](https://docs.green-coding.berlin/).
