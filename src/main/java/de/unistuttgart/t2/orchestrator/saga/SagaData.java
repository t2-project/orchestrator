package de.unistuttgart.t2.orchestrator.saga;

import de.unistuttgart.t2.orchestrator.domain.OrderDetails;

public class SagaData {

	private OrderDetails details;
	
	private String OrderId;

	
	public SagaData(OrderDetails details) {
		this.details = details;
		this.OrderId = null; // optional?
	}
	
	public SagaData() {
		
	}

	public OrderDetails getDetails() {
		return details;
	}

	public String getOrderId() {
		return OrderId;
	}

	public void setOrderId(String orderId) {
		OrderId = orderId;
	}
}
