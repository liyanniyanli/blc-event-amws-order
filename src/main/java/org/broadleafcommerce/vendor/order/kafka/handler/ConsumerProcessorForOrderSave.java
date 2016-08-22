package org.broadleafcommerce.vendor.order.kafka.handler;

import javax.annotation.Resource;

import org.broadleafcommerce.core.order.domain.Order;
import org.broadleafcommerce.core.order.service.OrderService;
import org.broadleafcommerce.core.pricing.service.exception.PricingException;
import org.broadleafcommerce.vendor.order.domain.OrderTranslate;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Service;

/**
 * This service is a consumer processor extends from ConsumerProcessor.
 * The consumer pull AMWS orders from KAFKA message poll and translate them 
 * to BLC order entities. Then save the entities to BLC.
 * 
 * @author JaneLi
 */
@Service("amazonOrderSave")
public class ConsumerProcessorForOrderSave extends ConsumerProcessor {

    @Resource(name="blOrderService")
    protected OrderService orderService;

	@Resource(name = "amazonOrderTranslate")
    protected OrderTranslate amazonOrderTranslate;
    
    public OrderService getOrderService() {
		return orderService;
	}

	public OrderTranslate getAmazonOrderTranslate() {
		return amazonOrderTranslate;
	}
	
	public ConsumerProcessorForOrderSave() {
		super();
	}

	/**
	 * Translate the received messages to BLC order entities.
	 * And then save the orders to BLC.
	 * @see ConsumerProcessor#process(org.springframework.messaging.Message)
	 */
	@Override
	public void process(Message<?> received) {
		logger.info("Order translate and save..........");
		// Get received messages. Messages are JSON formatted.
		Object orderInfo = received.getPayload();
		try {
			// Get the order list Array from the JSON string.
			JSONArray orderListJson = new JSONArray((String)orderInfo);
			for (int orderIndex = 0; orderIndex < orderListJson.length(); orderIndex++) {
				// Get the order object.
				JSONObject orderJson = orderListJson.getJSONObject(orderIndex);
				// Translate the order object to BLC order entity.
				logger.info("Order translate start..........");
				Order order = amazonOrderTranslate.translateToBlc(orderJson);
				// Save order entity to BLC.
				logger.info("Order save start..........");
				orderService.save(order, false);
			}
		} catch (PricingException e) {
			logger.error("Order save failed..........", e);
		} catch (JSONException e) {
			logger.error("JSON format error..........", e);
		}
	}

}
