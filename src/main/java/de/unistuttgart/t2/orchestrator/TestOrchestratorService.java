package de.unistuttgart.t2.orchestrator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import de.unistuttgart.t2.common.saga.SagaData;
import de.unistuttgart.t2.orchestrator.saga.Saga;
import io.eventuate.tram.sagas.orchestration.SagaInstanceFactory;

public class TestOrchestratorService extends OrchestratorService {

    private final Logger LOG = LoggerFactory.getLogger(getClass());
    
    @Autowired RestTemplate template;
    
    @Value("${t2.e2etest.url}")
    String testUrl;
    
	public TestOrchestratorService(SagaInstanceFactory sagaInstanceFactory, Saga saga) {
	    super(sagaInstanceFactory, saga);
	}

	/**
	 * creates the actual saga.
	 *  
	 * @param data
	 */
	@Override
	protected String createSaga(SagaData data) {
	    System.err.println("test service");
	    String id = super.createSaga(data);
	    
	    try {
	        template.postForObject(testUrl, id, Void.class);
	    } catch (RestClientException e) {
	        LOG.info(String.format("Failed to contact test service : %s", e.getMessage()));
	    }
	    
	    return id;
	}
}
