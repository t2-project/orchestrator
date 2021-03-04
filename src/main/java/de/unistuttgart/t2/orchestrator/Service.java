package de.unistuttgart.t2.orchestrator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClientException;

import de.unistuttgart.t2.common.PlaceOrderRequest;
import de.unistuttgart.t2.orchestrator.domain.OrderDetails;
import de.unistuttgart.t2.orchestrator.saga.Saga;
import de.unistuttgart.t2.orchestrator.saga.SagaData;
import io.eventuate.tram.sagas.orchestration.SagaInstanceFactory;

public class Service {

	@Autowired
	private SagaInstanceFactory sagaInstanceFactory;

	@Autowired
	private Saga saga;

	public Service(SagaInstanceFactory sagaInstanceFactory, Saga saga) {
		this.sagaInstanceFactory = sagaInstanceFactory;
		this.saga = saga;
	}

	/**
	 * THis gets called by frontend/Controller (in case of example) that is, this is
	 * where the saga starts!!
	 * 
	 * @param orderDetails
	 * @return irgendetwas
	 */

	public String foo(String id) {
		try {
			OrderDetails orderdetails = prepareOrderDetails(id);
			createSaga(orderdetails);
		} catch (RestClientException e) {
			return "REST FAILURE";
		} catch (Exception e) {
			return "WHATEVER OTHER FAILURE";
		}
		return "SUCCESS";
	}

	/**
	 * creates the actual saga.
	 * 
	 * this is a seperate operation because i'm not sure how @ Transactional cooperates with other things.
	 * 
	 * @param details
	 */
	@Transactional
	private void createSaga(OrderDetails details) {
		SagaData data = new SagaData(details);
		sagaInstanceFactory.create(saga, data);
	}

	/**
	 * get the reservations from inventory and prepare them into OrderDetails for
	 * the Saga.
	 * 
	 * inquire with REST/HTTP. if there's a timeout there'll be an exception.
	 * 
	 * @param id
	 * @return
	 */
	private OrderDetails prepareOrderDetails(String id) throws RestClientException {
		// TODO : call to inventory 
		return new OrderDetails();
	}

}
