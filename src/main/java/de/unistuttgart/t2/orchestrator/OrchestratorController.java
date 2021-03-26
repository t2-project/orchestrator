package de.unistuttgart.t2.orchestrator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OrchestratorController {

    private final Logger LOG = LoggerFactory.getLogger(getClass());
	
	private OrchestratorService service;

	@Autowired // HÄ??? What the fuck am i doing here???
	public OrchestratorController(OrchestratorService service) {
		this.service = service;
	}

    @ResponseStatus(HttpStatus.ACCEPTED)
	@PostMapping(value = "/order/{sessionId}")
	public void createOrder(@PathVariable String sessionId) {
    	LOG.info("received order request for session " + sessionId);
		
    	// start saga
    	service.createSaga(sessionId);
	}	
}