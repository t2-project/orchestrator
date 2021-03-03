package de.unistuttgart.t2.orchestrator.saga;

import de.unistuttgart.t2.common.commands.CheckCreditCommand;
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

	@Override
	public SagaDefinition<SagaData> getSagaDefinition() {
		// TODO Auto-generated method stub
		return this.sagaDefinition;
	}

	private SagaDefinition<SagaData> sagaDefinition = 
			step()
				.invokeParticipant(this::actionOrder)
				.onReply(OrderCreated.class, this::onReplayOrder)
				.onReply(OrderCreated.class, (a, b) -> System.err.println("order replied"))
				.withCompensation(this::compensationOrder)
			.step()
				.invokeParticipant(this::actionInventory)
				.onReply(Success.class, (a, b) -> System.err.println("inventory replied"))
				.withCompensation(this::compensationInventory).
			step()
				.invokeParticipant(this::actionPayment)
				.onReply(Success.class, (a, b) -> System.err.println("payment replied"))
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
		
		System.err.println("action order"); // TODO DELETE
		
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
		System.err.println("compensation order");
		
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
	 * create command that triggers inventory Service to decrease amount of given product
	 * 
	 * @param data
	 * @return
	 */
	private CommandWithDestination actionInventory(SagaData data) {
		
		System.err.println("compensation inventory"); // TODO DELETE
		
		String productId = data.getDetails().getProductId();
		int amount = data.getDetails().getAmount();
		
		return CommandWithDestinationBuilder.send(new DecreaseInventoryCommand(amount, productId))
				.to(DecreaseInventoryCommand.channel).build();
	}

	/**
	 * create command that triggers inventory Service to reincrease amount of given product
	 * 
	 * @param data
	 * @return
	 */
	private CommandWithDestination compensationInventory(SagaData data) {
		
		System.err.println("compensation inventory"); // TODO DELETE
		
		String productId = data.getDetails().getProductId();
		int amount = data.getDetails().getAmount();
		
		return CommandWithDestinationBuilder.send(new DecreaseInventoryCommand(amount, productId))
				.to(DecreaseInventoryCommand.channel).build();
	}

	/**
	 * trigger payment service to execute the payment
	 * 
	 * @param data
	 * @return
	 */
	private CommandWithDestination actionPayment(SagaData data) {
		
		System.err.println("action payment"); //DELETE
		
		String creditCardNumber = data.getDetails().getCreditCardNumber();
		double total = data.getDetails().getTotal();
		return CommandWithDestinationBuilder.send(new CheckCreditCommand(total, creditCardNumber)).to(CheckCreditCommand.channel).build();
	}

	/*
	 * failure handlers
	 */
	// TODO

}