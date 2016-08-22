package org.broadleafcommerce.vendor.order.kafka.configuration;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.expression.common.LiteralExpression;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.QueueChannel;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.kafka.inbound.KafkaMessageDrivenChannelAdapter;
import org.springframework.integration.kafka.outbound.KafkaProducerMessageHandler;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.listener.KafkaMessageListenerContainer;
import org.springframework.kafka.listener.config.ContainerProperties;
import org.springframework.kafka.support.TopicPartitionInitialOffset;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.PollableChannel;

/**
 * This class is the bean configuration for KAFKA.
 * 
 * @author JaneLi
 */
@Configuration
@EnableIntegration
@PropertySource("classpath:application.properties")
public class KafkaConfiguration {

	/**
	 * Auto-wire the environment properties.
	 */
	@Autowired
	Environment env;

	/**
	 * Handle messages for input channel.
	 * Input channel named toKafka.
	 */
	@Bean
	@ServiceActivator(inputChannel = "toKafka")
	public MessageHandler handler() throws Exception {
		KafkaProducerMessageHandler<String, String> handler =
				new KafkaProducerMessageHandler<>(kafkaTemplate());
		handler.setTopicExpression(new LiteralExpression(env.getProperty("kafka.topic")));
		handler.setMessageKeyExpression(new LiteralExpression(env.getProperty("kafka.messageKey")));
		return handler;
	}

	/**
	 * KAKFA template.
	 */
	@Bean
	public KafkaTemplate<String, String> kafkaTemplate() {
		return new KafkaTemplate<>(producerFactory());
	}

	/**
	 * KAFKA producer.
	 */
	@Bean
	public ProducerFactory<String, String> producerFactory() {
		Map<String, Object> props = new HashMap<>();
		props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, env.getProperty("kafka.broker.address"));
		props.put(ProducerConfig.RETRIES_CONFIG, 0);
		props.put(ProducerConfig.BATCH_SIZE_CONFIG, 16384);
		props.put(ProducerConfig.LINGER_MS_CONFIG, 1);
		props.put(ProducerConfig.BUFFER_MEMORY_CONFIG, 33554432);
		props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
		props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
		return new DefaultKafkaProducerFactory<>(props);
	}

	/**
	 * The listener for KAFKA message.
	 */
	@Bean
	public KafkaMessageListenerContainer<String, String> container() throws Exception {
		return new KafkaMessageListenerContainer<>(consumerFactory(),
				new ContainerProperties(new TopicPartitionInitialOffset(env.getProperty("kafka.topic"), 0)));
	}

	/**
	 * KAFKA consumer.
	 */
	@Bean
	public ConsumerFactory<String, String> consumerFactory() {
		Map<String, Object> props = new HashMap<>();
		props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, env.getProperty("kafka.broker.address"));
		props.put(ConsumerConfig.GROUP_ID_CONFIG, "siTestGroup");
		props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, true);
		props.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, 100);
		props.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, 15000);
		props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
		props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
		return new DefaultKafkaConsumerFactory<>(props);
	}

	/**
	 * Channel adapter for message.
	 */
	@Bean
	public KafkaMessageDrivenChannelAdapter<String, String>
				adapter(KafkaMessageListenerContainer<String, String> container) {
		KafkaMessageDrivenChannelAdapter<String, String> kafkaMessageDrivenChannelAdapter =
				new KafkaMessageDrivenChannelAdapter<>(container);
		kafkaMessageDrivenChannelAdapter.setOutputChannel(received());
		return kafkaMessageDrivenChannelAdapter;
	}

	/**
	 * Channel for KAFKA message received.
	 */
	@Bean
	public PollableChannel received() {
		return new QueueChannel();
	}

	/**
	 * KAFKA topic.
	 */
	@Bean
	public TopicCreator topicCreator() {
		return new TopicCreator(env.getProperty("kafka.topic"), env.getProperty("kafka.zookeeper.connect"));
	}
	
}
