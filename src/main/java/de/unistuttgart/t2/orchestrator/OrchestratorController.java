package de.unistuttgart.t2.orchestrator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import de.unistuttgart.t2.common.domain.saga.SagaData;
import de.unistuttgart.t2.common.domain.saga.SagaRequest;

@RestController
public class OrchestratorController {

	private final Logger LOG = LoggerFactory.getLogger(getClass());

	private OrchestratorService service;

	@Autowired // HÄ??? What the fuck am i doing here???
	public OrchestratorController(OrchestratorService service) {
		this.service = service;
	}

	@ResponseStatus(HttpStatus.ACCEPTED)
	@PostMapping(value = "/order")
	public void createOrder(@RequestBody SagaRequest request) {
		LOG.info(String.format("received order request for session %s", request.getSessionId()));

		assertRequest(request);
		
		// form data
		SagaData data = new SagaData(request.getCardNumber(), request.getCardOwner(),
				request.getChecksum(), request.getSessionId(), request.getTotal());

		// start saga
		service.createSaga(data);
	}

	private void assertRequest(SagaRequest request) {
	   if (request == null 
			   || request.getCardNumber() == null || request.getCardNumber().isEmpty() 
			   || request.getCardOwner() == null || request.getCardOwner().isEmpty()
			   || request.getChecksum() == null || request.getChecksum().isEmpty()
			   || request.getSessionId() == null  || request.getSessionId().isEmpty()
			   || request.getTotal() < 0) {
		   throw new IllegalArgumentException();
	   }
   }
}