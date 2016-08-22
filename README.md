# blc-event-amws-order
Extend broadleaf commerce with event to integration order features with AMWS through [spring-integration-kaka](https://github.com/spring-projects/spring-integration-kafka)

This modules use [spring-integration-kaka](https://github.com/spring-projects/spring-integration-kafka) to communicate with Kafka. And this is an annotation configuration sample.

There is a high-level project which is integrated with [Spring Cloud Stream](https://github.com/spring-cloud/spring-cloud-stream).  
Reference:   
1. [RactiveSW](https://github.com/reactivesw)  
2. [RactiveSW: AMWS-Product](https://github.com/reactivesw/amws_product)  
3. [RactiveSW: AMWS-Order](https://github.com/reactivesw/amws_order)  

## Step to enable extended entities

**Step 1.** Add these lines to your `core/resources/applicationContext-entity.xml`, for extended entity beans:

```
<bean id="org.broadleafcommerce.core.order.domain.Order" class="org.broadleafcommerce.vendor.order.domain.AmwsOrderImpl" scope="prototype"/>
<bean id="org.broadleafcommerce.vendor.order.domain.OrderAmazonAdditional" class="org.broadleafcommerce.vendor.order.domain.OrderAmazonAdditionalImpl" scope="prototype"/>
```

**Step 2.** Add entity persistence to your `core/resources/META-INF/persistence-core.xml`:

```
<class>org.broadleafcommerce.vendor.order.domain.AmwsOrderImpl</class>
<class>org.broadleafcommerce.vendor.order.domain.OrderAmazonAdditionalImpl</class>
```
>Note: These lines should be extend in `<persistence-unit name="blPU" transaction-type="RESOURCE_LOCAL"></persistence-unit>` and may append after `<non-jta-data-source>jdbc/web</non-jta-data-source>`

## Step to enable this module

**Step 1.** Add the dependency management section to your parent `pom.xml`(e.g. `admin/pom.xml`):

```
<dependency>
    <groupId>org.broadleafcommerce</groupId>
    <artifactId>blc-event-amws-order</artifactId>
    <version>1.0.0-GA</version>
    <type>jar</type>
    <scope>compile</scope>
</dependency>
```
**Step 2.** Pull this dependency into your `core/pom.xml`:

```
<dependency>
    <groupId>org.broadleafcommerce</groupId>
    <artifactId>blc-event-amws-order</artifactId>
</dependency>
```
**Step 3.** Include the necessary patchConfigLocation files in your `admin/web.xml`:

```
classpath:/blc-event-amws-order-applicationContext.xml
```
>Note: These two lines should go before the `classpath:/applicationContext.xml` line, but after `classpath:/bl-admin-applicationContext.xml`

**Step 4.** Include the necessary patchConfigLocation files in your `site/web.xml`:

```
classpath:/blc-event-amws-order-applicationContext.xml
```
>Note: This line should go before the `classpath:/applicationContext.xml` line

## Property Configuration

Kafka properties in `application.properties`:

```
kafka.topic=si.topic
kafka.messageKey=si.key
kafka.broker.address=localhost:9092
kafka.zookeeper.connect=localhost:2181
```
