package de.unistuttgart.t2.orchestrator.domain;

import javax.persistence.Embeddable;

@Embeddable
public class OrderDetails {
	
	private String creditCardNumber;
	private double total;
	private String productId;
	private int amount;
	
	public OrderDetails(String creditCardNumber, double total, String productId, int amount) {
		this.creditCardNumber = creditCardNumber;
		this.total = total;
		this.productId = productId;
		this.amount = amount;
	}

	public OrderDetails() {
	}

	public String getCreditCardNumber() {
		return creditCardNumber;
	}

	public double getTotal() {
		return total;
	}

	public String getProductId() {
		return productId;
	}

	public int getAmount() {
		return amount;
	}

}
