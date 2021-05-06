package de.unistuttgart.t2.orchestrator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;

import de.unistuttgart.t2.orchestrator.saga.Saga;

import io.eventuate.tram.sagas.orchestration.SagaInstanceFactory;
import io.eventuate.tram.sagas.spring.orchestration.SagaOrchestratorConfiguration;
import io.eventuate.tram.spring.consumer.kafka.EventuateTramKafkaMessageConsumerConfiguration;
import io.eventuate.tram.spring.messaging.producer.jdbc.TramMessageProducerJdbcConfiguration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@EnableJpaRepositories
@EnableAutoConfiguration
@Import({ TramMessageProducerJdbcConfiguration.class, 
		EventuateTramKafkaMessageConsumerConfiguration.class,
		SagaOrchestratorConfiguration.class })
public class OrchestratorApplication {

	public static void main(String[] args) {
		SpringApplication.run(OrchestratorApplication.class, args);
	}	

	@Profile("!test")
	@Bean
	public OrchestratorService orderService(SagaInstanceFactory sagaInstanceFactory, Saga saga) {
		return new OrchestratorService(sagaInstanceFactory, saga);
	}
	
	@Profile("test")
    @Bean
    public OrchestratorService testOrderService(SagaInstanceFactory sagaInstanceFactory, Saga saga) {
        return new TestOrchestratorService(sagaInstanceFactory, saga);
    }
	
	@Profile("test")
    @Bean
    public RestTemplate restTemplate() {
	    return new RestTemplate();
	}

	@Bean
	public Saga createSaga() {
		return new Saga();
	}
}
