package de.unistuttgart.t2.orchestrator.saga;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.unistuttgart.t2.common.saga.OrderCreatedReply;
import de.unistuttgart.t2.common.saga.SagaData;
import de.unistuttgart.t2.common.saga.commands.*;
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
				.onReply(OrderCreatedReply.class, this::onReplayOrder)
				.onReply(Success.class, this::onReplayOrderSuccess)
				.onReply(Success.class, (a, b) -> logger.info("order replied"))
				.withCompensation(this::compensationOrder)
			.step()
				.withCompensation(this::compensationInventory)
			.step()
				.invokeParticipant(this::actionPayment)
				.onReply(Success.class, (a, b) -> logger.info("payment replied"))
			.step()
				.invokeParticipant(this::actionInventory)
				.onReply(Success.class, (a, b) -> logger.info("inventory replied"))
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
		return CommandWithDestinationBuilder.send(new ActionCommand(data)).to(SagaCommand.order).build();
	}

	/**
	 * create command that triggers Order Service to reject an already created order
	 * 
	 * @param data
	 * @return
	 */
	private CommandWithDestination compensationOrder(SagaData data) {
		return CommandWithDestinationBuilder.send(new CompensationCommand(data)).to(SagaCommand.order).build();
	}

	/**
	 * 
	 * @param data
	 * @param reply
	 */
	private void onReplayOrder(SagaData data, OrderCreatedReply reply) {
		data.setOrderId(reply.getId());
	}
	
	/**
	 * 
	 * @param data
	 * @param reply
	 */
	private void onReplayOrderSuccess(SagaData data, Success reply) {
		data.setOrderId("success");
	}

	/**
	 * create command that triggers inventory Service to commit the reserved amount
	 * of products.
	 * 
	 * @param data
	 * @return
	 */
	private CommandWithDestination actionInventory(SagaData data) {
		return CommandWithDestinationBuilder.send(new ActionCommand(data)).to(SagaCommand.inventory).build();
	}

	/**
	 * create command that triggers inventory Service to delete the reserved amount
	 * of products.
	 * 
	 * @param data
	 * @return
	 */
	private CommandWithDestination compensationInventory(SagaData data) {
		return CommandWithDestinationBuilder.send(new CompensationCommand(data)).to(SagaCommand.inventory).build();
	}

	/**
	 * trigger payment service to execute the payment
	 * 
	 * @param data
	 * @return
	 */
	private CommandWithDestination actionPayment(SagaData data) {
		return CommandWithDestinationBuilder.send(new ActionCommand(data)).to(SagaCommand.payment).build();
	}

	/*
	 * failure handlers
	 */
	// TODO Maybe?

}