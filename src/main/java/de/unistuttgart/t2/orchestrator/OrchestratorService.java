package de.unistuttgart.t2.orchestrator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import de.unistuttgart.t2.common.domain.saga.SagaData;
import de.unistuttgart.t2.orchestrator.saga.Saga;
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
	 * @param details
	 */
	@Transactional
	protected void createSaga(SagaData data) {
		sagaInstanceFactory.create(saga, data);
	}
}
