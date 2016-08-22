package org.broadleafcommerce.vendor.order.kafka.handler;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.stereotype.Service;

@ComponentScan
@Service("amazonOrderProduceService")
public class ProducerProcessorForOrderCancel extends ProducerProcessor {
	
	public ProducerProcessorForOrderCancel() {
		super();
	}

	@Override
	public <T> GenericMessage<T> produceMessages(T payload) {
		GenericMessage<T> messages = new GenericMessage<>(payload);
		return messages;
	}

}
