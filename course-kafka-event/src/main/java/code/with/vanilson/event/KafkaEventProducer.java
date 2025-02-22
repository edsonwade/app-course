package code.with.vanilson.event;

import code.with.vanilson.config.ProducerConfig;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
            for (int i = 0; i < 100; i++) {
                String key = "truck_id" + i;
                String value = "Hello word " + i;
                try (KafkaProducer<String, String> producer = new KafkaProducer<>(props)) {
                    ProducerRecord<String, String> record = new ProducerRecord<>(TOPIC, key, value);
                    producer.send(record, (metadata, exception) -> {
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

}