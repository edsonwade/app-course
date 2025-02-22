### # Kafka Producer and Consumer with Different Data Types

This guide explains how to use Apache Kafka to send and receive messages with different data types, including lists and
collections, rather than just the default String key and String value.

## Prerequisites

- Apache Kafka installed and running
- Java Development Kit (JDK) installed
- Maven or Gradle for dependency management

## Dependencies

Make sure to include the following dependencies in your `pom.xml` (for Maven) or `build.gradle` (for Gradle):

### Maven

```xml

<dependency>
    <groupId>org.apache.kafka</groupId>
    <artifactId>kafka-clients</artifactId>
    <version>3.0.0</version> <!-- Use the latest version -->
</dependency>
<dependency>
<groupId>com.fasterxml.jackson.core</groupId>
<artifactId>jackson-databind</artifactId>
<version>2.12.3</version> <!-- Use the latest version -->
</dependency>
```

1. Using a Custom Serializer:
    - **Send data with List**: Using List<Producer>, you need to create a custom serializer that knows how to convert a
      `List<Product>` into a byte array.
    - This serializer would typically convert the list to a JSON string `(or another format)` and then serialize that
      string.

### Example of Custom Serializer for List

Sending `List<Product>` directly, you would need to implement a custom serializer. Here’s a simple example of what
that might look like:

```java
import org.apache.kafka.common.serialization.Serializer;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.Map;

public class ListProducerSerializer implements Serializer<List<Producer>> {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public byte[] serialize(String topic, List<Producer> data) {
        try {
            return objectMapper.writeValueAsBytes(data);
        } catch (Exception e) {
            throw new RuntimeException("Error serializing List<Producer>", e);
        }
    }

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
        // Configuration if needed
    }

    @Override
    public void close() {
        // Cleanup if needed
    }
}
```

### Update Producer Configuration

update producer configuration to use this custom serializer:

```java
import java.util.Properties;

public static Properties getKafkaProperties() {
    Properties props = new Properties();
    props.setProperty(BOOTSTRAP_SERVERS, HOSTNAME);
    props.setProperty(ACKS, "all");
    props.setProperty(RETRIES, Integer.toString(Integer.MAX_VALUE));
    props.setProperty(PRODUCER_KEY_SERIALIZER, StringSerializer.class.getName());
    props.setProperty(PRODUCER_VALUE_SERIALIZER, ListProducerSerializer.class.getName()); // Use your custom serializer
    props.setProperty("linger.ms.config", "3");
    props.setProperty("compression.type", "snappy");
    props.setProperty("batch.size", Integer.toString(32 * 1024));
    return props;
}

```

### Sending the Message

With the custom serializer in place, you can continue to send `List<Product>` as the value:

```java
public static void sendMessge() {
    Properties props = ProducerConfig.getKafkaProperties();
    for (int j = 0; j < 2; j++) {
        for (int i = 0; i < 100; i++) {
            String key = "truck_id" + i;
            try (KafkaProducer<String, List<Product>> producers = new KafkaProducer<>(props)) {
                ProducerRecord<String, List<Product>> record = new ProducerRecord<>(TOPIC, key, producers);
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

```

### Summary

- **Kafka can handle various data types**: You can send different types of data, but you must provide the correct
  serializer for that data type.

- **Custom Serializer**: If you want to send List<Producer>, you need to implement a custom serializer that knows how
  to convert that list into a byte array.

- **JSON String Option**: Alternatively, you can convert the list to a JSON string and use StringSerializer, which is
  simpler and often sufficient.
