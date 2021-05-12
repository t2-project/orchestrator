# Orchestrator Service 

This service is part of the T2 Store.
It orchestrates the saga.

See [LINK TO SPHINX DOCU](TODO) for more details on the saga.

## Build and Run Manually

_**TODO: copy from Order was mangus checked it.**_

## HTTP Endpoints
* ``/order`` POST here to start a saga 

## Usage

With this, you place an order for the user "foo", with the given payment details and a total cost of 42.0 money.
If the orchestrator Service successfully started a saga instance to handle the order, it returns the id of the saga instance. 

```
curl -i -X POST -H "Content-Type:application/json" -d '{"cardNumber":"num","cardOwner":"own","checksum":"sum", "sessionId":"foo", "total" : 42.0}' orchestrator-cs/order
```
```
000001796223c175-ee79d7ce3aa50000
```


## Application Properies

Properties for the CDC.
Confere [eventuate tram cdc](https://eventuate.io/docs/manual/eventuate-tram/latest/getting-started-eventuate-tram.html) for explanations.

property | read from env var |
-------- | ----------------- |
spring.datasource.url | SPRING_DATASOURCE_URL |
spring.datasource.username | SPRING_DATASOURCE_USERNAME |
spring.datasource.password | SPRING_DATASOURCE_PASSWORD |
spring.datasource.driver-class-name | SPRING_DATASOURCE_DRIVER_CLASS_NAME |
eventuatelocal.kafka.bootstrap.servers | EVENTUATELOCAL_KAFKA_BOOTSTRAP_SERVERS |
eventuatelocal.zookeeper.connection.string | EVENTUATELOCAL_ZOOKEEPER_CONNECTION_STRING |