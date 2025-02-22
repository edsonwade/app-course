package code.with.vanilson.config;

import code.with.vanilson.mapper.ListProducerSerializer;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;

import static code.with.vanilson.util.PropertyUtil.*;

/**
 * ProducerConfig
 *
 * @author vamuhong
 * @version 1.0
 * @since 2025-02-22
 */
public class ProducerConfig {

    private ProducerConfig() {
        throw new AssertionError("This class cannot be instantiated");
    }

    public static Properties getKafkaProducerProperties() {
        Properties props = new Properties();
        props.setProperty(BOOTSTRAP_SERVERS, HOSTNAME);
        props.setProperty(ACKS, "all");
        props.setProperty(RETRIES, Integer.toString(Integer.MAX_VALUE));
        props.setProperty(PRODUCER_KEY_SERIALIZER, StringSerializer.class.getName());
        props.setProperty(PRODUCER_VALUE_SERIALIZER, ListProducerSerializer.class.getName());
        props.setProperty("linger.ms.config", "3");
        props.setProperty("compression.type", "snappy");
        props.setProperty("batch.size", Integer.toString(32 * 1024));
        return props;
    }

}