package de.unistuttgart.t2.orchestrator.saga;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.unistuttgart.t2.common.commands.CheckCreditCommand;
import de.unistuttgart.t2.common.commands.CommitReservationCommand;
import de.unistuttgart.t2.common.commands.CreateOrderCommand;
import de.unistuttgart.t2.common.commands.DecreaseInventoryCommand;
import de.unistuttgart.t2.common.commands.RejectOrderCommand;
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
				.invokeParticipant(this::actionPayment)
				.onReply(Success.class, (a, b) -> logger.debug("payment replied"))
			.step()
				.invokeParticipant(this::actionInventory)
				.onReply(Success.class, (a, b) -> logger.debug("inventory replied"))
			.build();

	/*
	 * actions and rollbacks
	 */

	/**
	 * create command that triggers Order Service to create a new order
	 * 
	 * @param data
	 * @return
	 */
	private CommandWithDestination actionOrder(SagaData data) {
		
		logger.debug("action order"); // TODO DELETE
		
		String productId = data.getDetails().getProductId();
		int amount = data.getDetails().getAmount();
		double total = data.getDetails().getTotal();
		
		return CommandWithDestinationBuilder.send(new CreateOrderCommand(total, productId, amount))
				.to(CreateOrderCommand.channel).build();
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
		
		return CommandWithDestinationBuilder.send(new RejectOrderCommand(orderId))
				.to(RejectOrderCommand.channel).build();
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
		
		String id = data.getDetails().getProductId(); 
		
		return CommandWithDestinationBuilder.send(new CommitReservationCommand(id))
				.to(DecreaseInventoryCommand.channel).build();
	}

	/**
	 * trigger payment service to execute the payment
	 * 
	 * @param data
	 * @return
	 */
	private CommandWithDestination actionPayment(SagaData data) {
		
		logger.debug("action payment"); //DELETE
		
		String creditCardNumber = data.getDetails().getCreditCardNumber();
		double total = data.getDetails().getTotal();
		return CommandWithDestinationBuilder.send(new CheckCreditCommand(total, creditCardNumber)).to(CheckCreditCommand.channel).build();
	}

	/*
	 * failure handlers
	 */
	// TODO

}