package org.broadleafcommerce.vendor.order.kafka.handler;

import org.apache.log4j.Logger;
import org.broadleafcommerce.vendor.order.kafka.configuration.KafkaConfiguration;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.stereotype.Service;

@Service
@ComponentScan
public abstract class ProducerProcessor implements Runnable {
	
	  private static final Logger logger = Logger.getLogger(ProducerProcessor.class);

	  public abstract <T> GenericMessage<T> produceMessages(T payload);
	  
	  protected AnnotationConfigApplicationContext context;
	  
	  public ProducerProcessor() {
		  context = new AnnotationConfigApplicationContext(KafkaConfiguration.class);
	  }

	  @Override
	  public void run() {
		logger.info("MessageProducerProcessor start..........");
		MessageChannel toKafka = context.getBean("toKafka", MessageChannel.class);
		for (int i = 0; i < 10; i++) {
			toKafka.send(this.produceMessages("payload"+i));
		}
		try {
            System.out.println("Thread.sleep===========================");
            Thread.sleep(100000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
		context.close();
		logger.info("MessageProducerProcessor end..........");
	  }

}
