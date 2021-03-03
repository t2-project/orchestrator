package de.unistuttgart.t2.orchestrator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

import de.unistuttgart.t2.orchestrator.saga.Saga;

import io.eventuate.tram.sagas.orchestration.SagaInstanceFactory;
import io.eventuate.tram.sagas.spring.orchestration.SagaOrchestratorConfiguration;
import io.eventuate.tram.spring.consumer.kafka.EventuateTramKafkaMessageConsumerConfiguration;
//import io.eventuate.tram.consumer.rabbitmq.EventuateTramRabbitMQMessageConsumerConfiguration;
import io.eventuate.tram.spring.messaging.producer.jdbc.TramMessageProducerJdbcConfiguration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories
@EnableAutoConfiguration
//@Import(//, OptimisticLockingDecoratorConfiguration.class})
@Import({ TramMessageProducerJdbcConfiguration.class, 
		EventuateTramKafkaMessageConsumerConfiguration.class,
		//EventuateTramRabbitMQMessageConsumerConfiguration.class,
		SagaOrchestratorConfiguration.class })
public class OrchestratorApplication {

	public static void main(String[] args) {
		SpringApplication.run(OrchestratorApplication.class, args);
	}	

	@Bean
	public Service orderService(SagaInstanceFactory sagaInstanceFactory, Saga saga) {
		return new Service(sagaInstanceFactory, saga);
	}

	@Bean
	public Saga createSaga() {
		return new Saga();
	}
}
