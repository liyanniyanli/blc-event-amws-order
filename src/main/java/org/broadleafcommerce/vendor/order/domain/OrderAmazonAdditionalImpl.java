package org.broadleafcommerce.vendor.order.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.broadleafcommerce.common.copy.CreateResponse;
import org.broadleafcommerce.common.copy.MultiTenantCopyContext;
import org.broadleafcommerce.common.presentation.AdminPresentation;
import org.broadleafcommerce.core.order.domain.Order;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Index;
import org.hibernate.annotations.Parameter;

/**
 * The Class OrderAttributeImpl
 * @see OrderAmazonAdditional
 */
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name="BLC_ORDER_AMAZON_ADDITIONAL")
public class OrderAmazonAdditionalImpl implements OrderAmazonAdditional {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(generator= "OrderAmazonAdditionalId")
    @GenericGenerator(
        name="OrderAmazonAdditionalId",
        strategy="org.broadleafcommerce.common.persistence.IdOverrideTableGenerator",
        parameters = {
            @Parameter(name="segment_value", value="OrderAmazonAdditionalImpl"),
            @Parameter(name="entity_name", value="org.broadleafcommerce.core.catalog.domain.OrderAmazonAdditionalImpl")
        }
    )
    @Column(name = "ORDER_AMAZON_ADDITIONAL_ID")
    protected Long id;
    
    @Column(name = "AMAZON_ORDER_ID", nullable=false)
    @AdminPresentation(friendlyName = "OrderAmazonAdditionalImpl_Amazon_Order_Id", order=1000, prominent=true)
    protected String amazonOrderId;

    @Column(name = "CREATED_DATE")
    @AdminPresentation(friendlyName = "OrderAmazonAdditionalImpl_Created_Date", order=2000, prominent = true)
    protected Date createdDate;

    @Column(name = "MODIFIED_DATE")
    @AdminPresentation(friendlyName = "OrderAmazonAdditionalImpl_Modified_Date", order=3000, prominent = true)
    protected Date modifiedDate;
    
    @OneToOne(targetEntity = AmwsOrderImpl.class, optional=true)
    @JoinColumn(name = "ORDER_ID", nullable = true)
    @Index(name="ORDERAMAZONADDITIONAL_ORDER_INDEX", columnNames={"ORDER_ID"})
    @AdminPresentation(excluded = true)
    protected Order order;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

	@Override
	public String getAmazonOrderId() {
		return amazonOrderId;
	}

	@Override
	public void setAmazonOrderId(String amazonOrderId) {
		this.amazonOrderId = amazonOrderId;
	}

	@Override
	public Date getCreatedDate() {
		return createdDate;
	}

	@Override
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	@Override
	public Date getModifiedDate() {
		return modifiedDate;
	}

	@Override
	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

    @Override
    public Order getOrder() {
        return order;
    }

    @Override
    public void setOrder(Order order) {
        this.order = order;
    }

    @Override
    public <G extends OrderAmazonAdditional> CreateResponse<G> createOrRetrieveCopyInstance(MultiTenantCopyContext context) throws CloneNotSupportedException {
        CreateResponse<G> createResponse = context.createOrRetrieveCopyInstance(this);
        if (createResponse.isAlreadyPopulated()) {
            return createResponse;
        }
        OrderAmazonAdditional cloned = createResponse.getClone();
        cloned.setAmazonOrderId(amazonOrderId);
        cloned.setCreatedDate(createdDate);
        cloned.setModifiedDate(modifiedDate);
        //dont clone
        cloned.setOrder(order);
        return createResponse;
    }
}
