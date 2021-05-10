package de.unistuttgart.t2.orchestrator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import de.unistuttgart.t2.common.saga.SagaData;
import de.unistuttgart.t2.orchestrator.saga.Saga;
import io.eventuate.tram.sagas.orchestration.SagaInstance;
import io.eventuate.tram.sagas.orchestration.SagaInstanceFactory;

/**
 * Manages creation of new saga instances.
 * 
 * @author maumau
 *
 */
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
     * Creates a new saga instance.
     * 
	 * @param data informations to be passed to all participants
	 * @return id of saga instance
	 */
	@Transactional
	protected String createSaga(SagaData data) {
		return sagaInstanceFactory.create(saga, data).getId();
	}
}
