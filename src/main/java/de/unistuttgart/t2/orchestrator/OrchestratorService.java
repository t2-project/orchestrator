package de.unistuttgart.t2.orchestrator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClientException;

import de.unistuttgart.t2.orchestrator.domain.OrderDetails;
import de.unistuttgart.t2.orchestrator.saga.Saga;
import de.unistuttgart.t2.orchestrator.saga.SagaData;
import io.eventuate.tram.sagas.orchestration.SagaInstanceFactory;

public class OrchestratorService {

	@Autowired
	private SagaInstanceFactory sagaInstanceFactory;

	@Autowired
	private Saga saga;

	public OrchestratorService(SagaInstanceFactory sagaInstanceFactory, Saga saga) {
		this.sagaInstanceFactory = sagaInstanceFactory;
		this.saga = saga;
	}

	/**
	 * creates the actual saga.
	 * 
	 * TODO : what is  @ Transactional ?
	 * 
	 * @param details
	 */
	@Transactional
	protected void createSaga(String sessionId) {
		SagaData data = new SagaData(sessionId);
		sagaInstanceFactory.create(saga, data);
	}
}
