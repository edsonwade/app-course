package code.with.vanilson.config;

import org.apache.kafka.clients.consumer.CooperativeStickyAssignor;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.util.Properties;

import static code.with.vanilson.util.PropertyUtil.*;

/**
 * ProducerConfig
 *
 * @author vamuhong
 * @version 1.0
 * @since 2025-02-22
 */
public class ConsumerConfig {


    private ConsumerConfig() {
        throw new AssertionError("This class cannot be instantiated");
    }

    public static Properties getKafkaProducerProperties() {
        Properties properties = new Properties();
        properties.setProperty(BOOTSTRAP_SERVERS, HOSTNAME);
        properties.setProperty(AUTO_OFFSET_RESET_CONFIG, OFFSET_RESET); // consume from the first offset
        properties.setProperty(GROUP_ID_CONFIG, GROUP_ID);
        properties.setProperty(ENABLE_AUTO_COMMIT, String.valueOf(Boolean.TRUE));
        properties.setProperty(CONSUMER_KEY_DESERIALIZER, StringDeserializer.class.getName());
        properties.setProperty(CONSUMER_VALUE_DESERIALIZER, StringDeserializer.class.getName());
        properties.setProperty(PARTITION_ASSIGNMENT_STRATEGY, CooperativeStickyAssignor.class.getName()); // Podes

        return properties;
    }

}