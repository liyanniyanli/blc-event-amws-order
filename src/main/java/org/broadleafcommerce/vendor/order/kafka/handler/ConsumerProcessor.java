package org.broadleafcommerce.vendor.order.kafka.handler;

import org.apache.log4j.Logger;
import org.broadleafcommerce.vendor.order.kafka.configuration.KafkaConfiguration;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.messaging.Message;
import org.springframework.messaging.PollableChannel;

/**
 * This class is an abstract class of the consumer processor. 
 * This consumer pull AMWS orders from KAFKA message poll.
 * And the consume process is an abstract method.
 * 
 * @author JaneLi
 */
public abstract class ConsumerProcessor implements Runnable {
	
	  protected static final Logger logger = Logger.getLogger(ConsumerProcessor.class);

	  /**
	   * Abstract method for sub class override.
	   * This method do something to consume the received messages.
	   */
	  public abstract void process(Message<?> received);
	  
	  protected AnnotationConfigApplicationContext context;
	  
	  /**
	   * Construct method initial the context with KAFKA configuration.
	   */
	  public ConsumerProcessor() {
		  context = new AnnotationConfigApplicationContext(KafkaConfiguration.class);
	  }

	  /**
	   * Pull messages from channel received and do something with them.
	   * @see java.lang.Runnable#run()
	   */
	  @Override
	  public void run() {
        logger.info("MessageConsumerProcessor start..........");
        // Get the PollableChannel. 
		PollableChannel fromKafka = context.getBean("received", PollableChannel.class);
		while (true) {
			// Receive messages from the channel.
			Message<?> received = fromKafka.receive(10000);
			if (received != null) {
				// Process something with the messages...
				this.process(received);
			}
		}
	  }

}
