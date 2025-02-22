package code.with.vanilson.event;

import code.with.vanilson.config.ProducerConfig;
import code.with.vanilson.producer.Product;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.List;
import java.util.Properties;

import static code.with.vanilson.util.PropertyUtil.TOPIC;

/**
 * KafkaEventProducer
 *
 * @author vamuhong
 * @version 1.0
 * @since 2025-02-22
 */
public class KafkaEventProducer {
    private static final Logger log = LoggerFactory.getLogger(KafkaEventProducer.class);

    private KafkaEventProducer() {
        throw new AssertionError("This class cannot be instantiated");
    }

    public static void sendMessge() {
        Properties props = ProducerConfig.getKafkaProperties();
        for (int j = 0; j < 2; j++) {
            for (int i = 0; i < 10; i++) {
                String key = "truck_id " + i;
                List<Product> value = getProducts();
                try (KafkaProducer<String, List<Product>> producer = new KafkaProducer<>(props)) {
                    ProducerRecord<String, List<Product>> listRecord  = new ProducerRecord<>(TOPIC, key, value);
                    producer.send(listRecord , (metadata, exception) -> {
                        if (exception != null) {
                            log.error("Error sending record", exception);
                        } else {
                            log.info("Sent message with key: {} | partition: {} |, offset: {} |, timestamp: {} |", key,
                                    metadata.partition(),
                                    metadata.offset(),
                                    metadata.timestamp());

                        }
                    });
                    Thread.sleep(500);
                    producer.flush();
                } catch (InterruptedException e) {
                    log.error("Thread exception error ", e);
                    Thread.currentThread().interrupt();
                }
            }
        }

    }

    private static List<Product> getProducts() {
        return List.of(
                new Product("Tv", 12, BigDecimal.valueOf(100_00), "v1"),
                new Product("Xbox", 2, BigDecimal.valueOf(12000.9999), "v1"),
                new Product("Mobile", 5, BigDecimal.valueOf(13.000), "v2"),
                new Product("Books", 5, BigDecimal.valueOf(100), "v2"),
                new Product("Headphones", 23, BigDecimal.valueOf(45.99), "v3"),
                new Product("Monitor", 3, BigDecimal.valueOf(1000.898), "v5"),
                new Product("Gamepad", 11, BigDecimal.valueOf(56.99), "v6")
        );
    }

}