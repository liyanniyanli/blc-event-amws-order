package org.broadleafcommerce.vendor.order.domain;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.broadleafcommerce.common.currency.domain.BroadleafCurrency;
import org.broadleafcommerce.common.currency.domain.BroadleafCurrencyImpl;
import org.broadleafcommerce.common.locale.domain.Locale;
import org.broadleafcommerce.common.locale.domain.LocaleImpl;
import org.broadleafcommerce.common.money.Money;
import org.broadleafcommerce.core.catalog.service.CatalogService;
import org.broadleafcommerce.core.offer.domain.CandidateOrderOffer;
import org.broadleafcommerce.core.offer.domain.Offer;
import org.broadleafcommerce.core.offer.domain.OfferInfo;
import org.broadleafcommerce.core.order.domain.FulfillmentGroup;
import org.broadleafcommerce.core.order.domain.FulfillmentGroupImpl;
import org.broadleafcommerce.core.order.domain.OrderAttribute;
import org.broadleafcommerce.core.order.domain.OrderItem;
import org.broadleafcommerce.core.order.service.call.ActivityMessageDTO;
import org.broadleafcommerce.core.order.service.type.FulfillmentType;
import org.broadleafcommerce.core.order.service.type.OrderStatus;
import org.broadleafcommerce.core.payment.domain.OrderPayment;
import org.broadleafcommerce.profile.core.domain.Address;
import org.broadleafcommerce.profile.core.domain.AddressImpl;
import org.broadleafcommerce.profile.core.domain.Customer;
import org.broadleafcommerce.profile.core.domain.CustomerImpl;
import org.broadleafcommerce.profile.core.domain.Phone;
import org.broadleafcommerce.profile.core.domain.PhoneImpl;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.stereotype.Service;

@Service("amazonOrderTranslate")
public class OrderTranslate {
	
    @Resource(name = "blCatalogService")
    private CatalogService catalogService;

	public AmwsOrderImpl translateToBlc(JSONObject amwsOrderObject) throws JSONException {
		
		AmwsOrderImpl order = new AmwsOrderImpl();
		
//		order.setAuditable(auditable);
		order.setPreview(true);
//		order.setName(amwsOrderObject.getString("name"));
		order.setName("testAmazon001");
		// BuyerName
		// BuyerEmail
		Customer orderCustomer = new CustomerImpl();
		orderCustomer.setId(1L);
		order.setCustomer(orderCustomer);
		
		String orderStatus = amwsOrderObject.getString("orderStatus");
		order.setStatus(new OrderStatus(orderStatus.toUpperCase(), orderStatus));
//		order.setTotalTax(new Money(amwsOrderObject.getDouble("totalTax")));
		order.setTotalTax(new Money(0.09));
//		order.setTotalFulfillmentCharges(new Money(amwsOrderObject.getDouble("totalFulfillmentCharges")));
		order.setTotalFulfillmentCharges(new Money(1.00));
//		order.setSubTotal(new Money(amwsOrderObject.getDouble("subTotal")));
		order.setSubTotal(new Money(300.00));
		
//		JSONObject orderTotal = amwsOrderObject.getJSONObject("orderTotal");
//		BroadleafCurrency totalCurrency = new BroadleafCurrencyImpl();
//		totalCurrency.setCurrencyCode(orderTotal.getString("currencyCode"));
//		BigDecimal totalAmount = new BigDecimal(orderTotal.getDouble("amount"));
		BigDecimal totalAmount = new BigDecimal(300.00);
//		order.setTotal(new Money(totalAmount, totalCurrency));
		order.setTotal(new Money(totalAmount));
		
//		order.setSubmitDate(new Date(amwsOrderObject.getLong("submitDate")));
//		order.setOrderNumber(amwsOrderObject.getString("orderNumber"));
//		order.setOrderSource(amwsOrderObject.getString("orderSource"));
		List<OrderItem> orderItems = new ArrayList<>();
//		JSONArray itemArray = amwsOrderObject.getJSONArray("orderItems");
//		for (int itemIndex = 0; itemIndex < itemArray.length(); itemIndex++) {
//			JSONObject orderItemObject = itemArray.getJSONObject(itemIndex);
//			DiscreteOrderItemImpl orderItem = new DiscreteOrderItemImpl();
//			orderItem.setName(orderItemObject.getString("title"));
//
//			Long skuId = Long.parseLong(orderItemObject.getString("sellerSku"));
//	        Sku itemSku = catalogService.findSkuById(skuId);
//	        orderItem.setSku(itemSku);
//	        
//			JSONObject itemPrice = orderItemObject.getJSONObject("itemPrice");
//			BroadleafCurrency priceCurrency = new BroadleafCurrencyImpl();
//			priceCurrency.setCurrencyCode(itemPrice.getString("currencyCode"));
//			BigDecimal priceAmount = new BigDecimal(itemPrice.getDouble("amount"));
//			orderItem.setPrice(new Money(priceAmount, priceCurrency));
//			orderItem.setQuantity(orderItemObject.getInt("quantityOrdered"));
//			orderItems.add(orderItem);
//		}
		order.setOrderItems(orderItems);

		List<FulfillmentGroup> fulfillmentGroups = new ArrayList<>();
		FulfillmentGroup fufillmentGroup = new FulfillmentGroupImpl();
		String fulfillmentChannel = amwsOrderObject.getString("fulfillmentChannel");
		FulfillmentType fulfillmentType = new FulfillmentType(fulfillmentChannel.toUpperCase(), fulfillmentChannel);
		fufillmentGroup.setType(fulfillmentType);

		Address address = new AddressImpl();
		address.setCounty("JP");
		// stateOrRegion
		// district
		address.setCity("Tokyo");
		address.setPostalCode("107-0053");
		address.setAddressLine1("1-2-10 Akasaka");
		address.setAddressLine2("");
		address.setAddressLine3("");
		address.setFullName("June Smith");
		Phone phone = new PhoneImpl();
		phone.setCountryCode("JP");
		phone.setPhoneNumber("407-9999999");
		address.setPhonePrimary(phone);
		fufillmentGroup.setAddress(address);
		fufillmentGroup.setDeliveryInstruction("");
		fufillmentGroup.setTotal(new Money(20.00));
		fufillmentGroup.setTotalTax(new Money(0.79));
		fufillmentGroup.setOrder(order);
//		JSONObject shippingAddress = amwsOrderObject.getJSONObject("shippingAddress");
//		Address address = new AddressImpl();
//		address.setCounty(shippingAddress.getString("country"));
//		// stateOrRegion
//		// district
//		address.setCity(shippingAddress.getString("city"));
//		address.setPostalCode(shippingAddress.getString("postalCode"));
//		address.setAddressLine1(shippingAddress.getString("addressLine1"));
//		address.setAddressLine2(shippingAddress.getString("addressLine2"));
//		address.setAddressLine3(shippingAddress.getString("addressLine3"));
//		address.setFullName(shippingAddress.getString("name"));
//		Phone phone = new PhoneImpl();
//		phone.setCountryCode(shippingAddress.getString("countryCode"));
//		phone.setPhoneNumber(shippingAddress.getString("phone"));
//		address.setPhonePrimary(phone);
//      fufillmentGroup.setAddress(address);
		// shipServiceLevel
		// NumberOfItemsShipped
		// NumberOfItemsUnshipped
		// ShipmentServiceLevelCategory
		fulfillmentGroups.add(fufillmentGroup);
		order.setFulfillmentGroups(fulfillmentGroups);
		
		
		List<CandidateOrderOffer> candidateOrderOffers = new ArrayList<>();
		order.setCandidateOrderOffers(candidateOrderOffers);

		List<OrderPayment> payments = new ArrayList<>();
//		JSONArray paymentArray = amwsOrderObject.getJSONArray("paymentExecutionDetail");
//		for (int paymentIndex = 0; paymentIndex < paymentArray.length(); paymentIndex++) {
//			JSONObject paymentDetailItem = paymentArray.getJSONObject(paymentIndex);
//			JSONObject paymentItemAmount = paymentDetailItem.getJSONObject("payment");
//			BroadleafCurrency paymentCurrency = new BroadleafCurrencyImpl();
//			paymentCurrency.setCurrencyCode(paymentItemAmount.getString("currencyCode"));
//			BigDecimal paymentAmount = new BigDecimal(paymentItemAmount.getDouble("amount"));
//			OrderPayment payment = new OrderPaymentImpl();
//			payment.setAmount(new Money(paymentAmount, paymentCurrency));
//			String paymentMethod = paymentDetailItem.getString("paymentMethod");
//			PaymentType paymentType = new PaymentType(paymentMethod.toUpperCase(), paymentMethod);
//			payment.setType(paymentType);
////			payment.setType(PaymentType.getInstance(paymentDetails.getString("paymentMethod")));
//			payments.add(payment);
//		}
		order.setPayments(payments);
		// paymentMethod
		
		Map<Offer, OfferInfo> additionalOfferInformation = new HashMap<>();
		order.setAdditionalOfferInformation(additionalOfferInformation);
		Map<String, OrderAttribute> orderAttributes = new HashMap<>();
		order.setOrderAttributes(orderAttributes);
		BroadleafCurrency currency = new BroadleafCurrencyImpl();
		currency.setCurrencyCode("JPY");
		currency.setFriendlyName("Japanese Yuan");
		order.setCurrency(currency);
		Locale locale = new LocaleImpl();
		locale.setLocaleCode("jp");
		order.setLocale(locale);
		
		OrderAmazonAdditional orderAmazonAdditional = new OrderAmazonAdditionalImpl();
		orderAmazonAdditional.setAmazonOrderId(amwsOrderObject.getString("amazonOrderId"));
		orderAmazonAdditional.setCreatedDate(new Date());
		orderAmazonAdditional.setModifiedDate(new Date());
		// purchaseData
		// lastUpdateDate
		// orderType
		// marketplaceId
		// BuyerName
		// BuyerEmail
		// SalesChannel
		// OrderChannel
		// EarliestShipDate
		// LatestShipDate
		// SellerOrderId
		orderAmazonAdditional.setOrder(order);
		order.setOrderAmazonAdditional(orderAmazonAdditional);
		
		order.setTaxOverride(false);
		List<ActivityMessageDTO> orderMessages = new ArrayList<>();
		order.setOrderMessages(orderMessages);
		
		return order;
	}
	
}
