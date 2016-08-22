package org.broadleafcommerce.vendor.order;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.broadleafcommerce.vendor.order.kafka.handler.ConsumerProcessor;
import org.broadleafcommerce.vendor.order.kafka.handler.ConsumerProcessorForOrderSave;
import org.springframework.stereotype.Service;

/**
 * This service create a new task to run the consumer processor. 
 * This consumer pull AMWS orders from KAFKA message poll.
 * And then translate the orders to BLC order entities and save them to BLC system.
 * 
 * @author JaneLi
 */
@Service("pullAmazonOrder")
public class PullAmwsOrderTask 
{
	protected static final Logger logger = Logger.getLogger(ConsumerProcessor.class);
	
	/**
	 * Defined a ConsumerProcessorForOrderSave service.
	 */
	@Resource(name = "amazonOrderSave")
	ConsumerProcessorForOrderSave orderMessageConsumer;
	
    public ConsumerProcessorForOrderSave getOrderMessageConsumer() {
		return orderMessageConsumer;
	}

    /**
     * This method run the runnable thread of ConsumerProcessorForOrderSave service.
     */
	public void runPullOrderTask()
    {
		logger.info("Order pull start..........");
        orderMessageConsumer.run();
    }
}

