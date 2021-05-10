package de.unistuttgart.t2.orchestrator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import de.unistuttgart.t2.common.saga.SagaData;
import de.unistuttgart.t2.common.saga.SagaRequest;

/**
 * Defines the http enpoints of the orchestrator service.
 * 
 * @author maumau
 *
 */
@RestController
public class OrchestratorController {

	private final Logger LOG = LoggerFactory.getLogger(getClass());
	
	@Autowired 
	OrchestratorService service;

	/**
	 * Starts a saga.
	 * <p>
	 * Replies as soon as the saga is created.
	 * 
	 * @param request request to start a saga
	 * @return id of saga instance
	 */
	@ResponseStatus(HttpStatus.ACCEPTED)
	@PostMapping(value = "/order")
	public String createOrder(@RequestBody SagaRequest request) {
		LOG.info(String.format("received order request for session %s", request.getSessionId()));

		assertRequest(request);
		
		SagaData data = new SagaData(request.getCardNumber(), request.getCardOwner(),
				request.getChecksum(), request.getSessionId(), request.getTotal());

		return service.createSaga(data);
	}

	/**
	 * Assert that all information required for the saga are provided.
	 * 
	 * @param request request to start a saga
	 */
	private void assertRequest(SagaRequest request) {
	   if (request == null 
			   || request.getCardNumber() == null || request.getCardNumber().isEmpty() 
			   || request.getCardOwner() == null || request.getCardOwner().isEmpty()
			   || request.getChecksum() == null || request.getChecksum().isEmpty()
			   || request.getSessionId() == null  || request.getSessionId().isEmpty()
			   || request.getTotal() < 0) {
		   throw new IllegalArgumentException("illegal saga request : " + request.toString());
	   }
   }
}