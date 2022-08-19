package de.unistuttgart.t2.orchestrator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.*;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import de.unistuttgart.t2.orchestrator.saga.Saga;
import io.eventuate.tram.sagas.orchestration.SagaInstanceFactory;
import io.eventuate.tram.sagas.spring.orchestration.SagaOrchestratorConfiguration;
import io.eventuate.tram.spring.consumer.kafka.EventuateTramKafkaMessageConsumerConfiguration;
import io.eventuate.tram.spring.messaging.producer.jdbc.TramMessageProducerJdbcConfiguration;
import io.eventuate.tram.spring.optimisticlocking.OptimisticLockingDecoratorConfiguration;
import io.swagger.v3.oas.models.*;
import io.swagger.v3.oas.models.info.Info;

/**
 * Orchestrates distributed transactions according to the saga pattern.
 *
 * @author maumau
 */
@SpringBootApplication
@EnableJpaRepositories
@Import({ TramMessageProducerJdbcConfiguration.class, EventuateTramKafkaMessageConsumerConfiguration.class,
          SagaOrchestratorConfiguration.class, OptimisticLockingDecoratorConfiguration.class })
public class OrchestratorApplication {

    public static void main(String[] args) {
        SpringApplication.run(OrchestratorApplication.class, args);
    }

    @Bean
    public OrchestratorService orderService(SagaInstanceFactory sagaInstanceFactory, Saga saga) {
        return new OrchestratorService(sagaInstanceFactory, saga);
    }

    @Bean
    public Saga createSaga() {
        return new Saga();
    }

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI().components(new Components()).info(new Info().title("Orchestrator service API")
            .description("API of the T2-Project's orchestrator service."));
    }
}
