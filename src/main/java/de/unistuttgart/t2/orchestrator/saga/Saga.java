package de.unistuttgart.t2.orchestrator.saga;


import java.time.Instant;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.unistuttgart.t2.common.commands.inventory.*;
import de.unistuttgart.t2.common.commands.order.*;
import de.unistuttgart.t2.common.commands.payment.*;
import de.unistuttgart.t2.common.replies.OrderCreated;
import io.eventuate.tram.commands.common.Success;
import io.eventuate.tram.commands.consumer.CommandWithDestination;
import io.eventuate.tram.commands.consumer.CommandWithDestinationBuilder;
import io.eventuate.tram.sagas.orchestration.SagaDefinition;
import io.eventuate.tram.sagas.simpledsl.SimpleSaga;


public class Saga implements SimpleSaga<SagaData> {

    Logger logger = LoggerFactory.getLogger(Saga.class);
	
	@Override
	public SagaDefinition<SagaData> getSagaDefinition() {
		// TODO Auto-generated method stub
		return this.sagaDefinition;
	}

	private SagaDefinition<SagaData> sagaDefinition = 
			step()
				.invokeParticipant(this::actionOrder)
				.onReply(OrderCreated.class, this::onReplayOrder)
				.onReply(OrderCreated.class, (a, b) -> logger.debug("order replied"))
				.withCompensation(this::compensationOrder)
			.step()
				.invokeParticipant(this::actionInventory)
				.onReply(Success.class, (a, b) -> logger.debug("inventory replied"))
			.step()
				.invokeParticipant(this::actionPayment)
				.onReply(Success.class, (a, b) -> logger.debug("payment replied"))
			.build();

	/*
	 * actions and compensations
	 */

	/**
	 * create command that triggers Order Service to create a new order
	 * 
	 * @param data
	 * @return
	 */
	private CommandWithDestination actionOrder(SagaData data) {
		
		logger.debug("action order"); // TODO DELETE
		
		String sessionId = data.getSessionId();
		Date timestamp = Date.from(Instant.now());
		logger.debug("now : " + timestamp.toString());
		
		return CommandWithDestinationBuilder.send(new OrderAction(sessionId, timestamp))
				.to(OrderAction.channel).build();
	}

	/**
	 * create command that triggers Order Service to reject an already created order
	 * 
	 * @param data
	 * @return
	 */
	private CommandWithDestination compensationOrder(SagaData data) {
		// TODO
		logger.debug("compensation order");
		
		String orderId = data.getOrderId();
		
		return CommandWithDestinationBuilder.send(new OrderCompensation(orderId))
				.to(OrderCompensation.channel).build();
	}

	/**
	 * 
	 * @param data
	 * @param reply
	 */
	private void onReplayOrder(SagaData data, OrderCreated reply) {
		data.setOrderId(reply.getId());
	}

	/**
	 * create command that triggers inventory Service to commit the reserved amount of products.
	 * 
	 * @param data
	 * @return
	 */
	private CommandWithDestination actionInventory(SagaData data) {
		
		logger.debug("action inventory"); // TODO DELETE
				
		return CommandWithDestinationBuilder.send(new InventoryAction())
				.to(InventoryCommand.channel).build();
	}
	
	private CommandWithDestination compensationInventory(SagaData data) {
		// TODO
		logger.debug("compensation inventory");
		
		return CommandWithDestinationBuilder.send(new InventoryCompensation())
				.to(InventoryCommand.channel).build();
	}

	/**
	 * trigger payment service to execute the payment
	 * 
	 * @param data
	 * @return
	 */
	private CommandWithDestination actionPayment(SagaData data) {
		logger.debug("action payment"); //DELETE
		
		return CommandWithDestinationBuilder.send(new PaymentAction(data.getSessionId())).to(PaymentAction.channel).build();
	}

	/*
	 * failure handlers
	 */
	// TODO

}