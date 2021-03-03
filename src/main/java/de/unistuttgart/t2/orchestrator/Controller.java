package de.unistuttgart.t2.orchestrator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import de.unistuttgart.t2.common.PlaceOrderRequest;
import de.unistuttgart.t2.orchestrator.domain.OrderDetails;

@RestController
public class Controller {

	private Service service;

	@Autowired
	public Controller(Service service) {
		this.service = service;
	}

	/**
	 * 
	 * 
	 * TODO: String -> an actual response / request (classes)
	 * 
	 * @param createOrderRequest
	 * @return 
	 */
	@RequestMapping(value = "/orders", method = RequestMethod.POST)
	public String createOrder(@RequestBody PlaceOrderRequest request) {
		
		String reply = service.foo(new OrderDetails(request.getCreditCardNumber(), request.getTotal(), request.getProductId(), request.getAmount()));
		return reply;
	}
	
	@RequestMapping(value = "/foo", method = RequestMethod.GET)
	public String foo() {
		String reply = service.foo((new OrderDetails()));
		return "fooo" + reply;
	}
	
	
}