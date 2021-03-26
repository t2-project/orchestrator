package de.unistuttgart.t2.orchestrator.saga;

import java.util.Map;

import de.unistuttgart.t2.orchestrator.domain.OrderDetails;

public class SagaData {

	private OrderDetails details;
	
	private String orderId;
	private String sessionId;

	
	public SagaData(String sessionId) {
		this.sessionId = sessionId;
	}
	
	public SagaData() {
		
	}

	public OrderDetails getDetails() {
		return details;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}
}
