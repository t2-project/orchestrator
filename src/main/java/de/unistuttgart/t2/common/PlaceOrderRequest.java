package de.unistuttgart.t2.common;

/**
 * An incomming request to place an Order.
 * 
 * Contains all relevant information.
 *  
 * @author maumau
 *
 */
public class PlaceOrderRequest {
	
	public PlaceOrderRequest(String creditCardNumber, double total, String productId, int amount) {
		this.creditCardNumber = creditCardNumber;
		this.total = total;
		this.productId = productId;
		this.amount = amount;
	}

	private String creditCardNumber;
	private double total;
	private String productId;
	private int amount;
	
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
