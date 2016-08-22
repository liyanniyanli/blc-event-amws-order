package org.broadleafcommerce.vendor.order.domain;

import java.io.Serializable;
import java.util.Date;

import org.broadleafcommerce.common.copy.MultiTenantCloneable;
import org.broadleafcommerce.core.order.domain.Order;

/**
 * The Interface OrderAmazonAdditional.   
 * Allows for arbitrary data to be
 * persisted with the order.
 *
 */
public interface OrderAmazonAdditional extends Serializable, MultiTenantCloneable<OrderAmazonAdditional> {

    /**
     * Gets the id.
     * 
     * @return the id
     */
    Long getId();

    /**
     * Sets the id.
     * 
     * @param id the new id
     */
    void setId(Long id);
    
    /**
     * Gets the amazon order id.
     * 
     * @return the amazon order id
     */
    String getAmazonOrderId();

    /**
     * Sets the amazon order id.
     * 
     * @param amazonOrderId the amazon order id
     */
    void setAmazonOrderId(String amazonOrderId);

    /**
     * Gets the created date.
     * 
     * @return the created date
     */
    Date getCreatedDate();

    /**
     * Sets the created date.
     * 
     * @param createdDate the created date
     */
    void setCreatedDate(Date createdDate);   

    /**
     * Gets the modified date.
     * 
     * @return the modified date
     */
    Date getModifiedDate();

    /**
     * Sets the modified date.
     * 
     * @param modifiedDate the modified date
     */
    void setModifiedDate(Date modifiedDate);   

    /**
     * Gets the associated order.
     * 
     * @return the order
     */
    Order getOrder();

    /**
     * Sets the order.
     * 
     * @param order the associated order
     */
    void setOrder(Order order);
}
