package de.unistuttgart.t2.orchestrator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

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
	   * THis gets called by frontend/Controller (in case of example) that is, this is where the saga starts!!
	   * @param orderDetails
	   * @return
	   */
	  @Transactional
	  public String foo(OrderDetails details) {
	    SagaData data = new SagaData(details);
	    sagaInstanceFactory.create(saga, data);
	    return "insert order details here :D";
	  }

}
