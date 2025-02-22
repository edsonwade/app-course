### Key Points to Understand:

1. **Serializer Requirement**: Kafka requires that both the key and value of a message be serialized into a byte format
   before they can be sent over the network. This is done using serializers, which are classes that implement
   the `Serializer` interface.

2. **Default Serializers**: In your configuration, you have set the key serializer to `StringSerializer`, which is
   correct for your key (a `String`). However, for the value, you are using `ListSerializer`, which is not a standard
   serializer provided by Kafka. This is likely the source of your error.

3. **Custom Serialization**: Since `List<Producer>` is not a primitive type or a standard type that Kafka knows how to
   serialize, you need to either:
    - Create a custom serializer for the `List<Producer>` type.
    - Use a different data structure that can be easily serialized, such as a JSON string representation of the list.

### Suggested Solutions:

1. **Custom Serializer**: If you want to keep using `List<Producer>`, you will need to implement a custom serializer
   that knows how to convert a `List<Producer>` into a byte array. This typically involves converting the list to a JSON
   string (using a library like Jackson or Gson) and then serializing that string.

2. **Use a Different Data Structure**: If you don't want to implement a custom serializer, consider changing the value
   type to something that can be easily serialized, such as a JSON string. You can convert your list of `Producer`
   objects to a JSON string before sending it to Kafka.

### Example of Custom Serialization:

If you decide to go with a custom serializer, it might look something like this:

```java
public class ListProducerSerializer implements Serializer<List<Producer>> {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public byte[] serialize(String topic, List<Producer> data) {
        try {
            return objectMapper.writeValueAsBytes(data);
        } catch (JsonProcessingException e) {
            throw new SerializationException("Error serializing List<Producer>", e);
        }
    }
}
```

Then, you would set this custom serializer in your Kafka properties:

```bash
props.setProperty(PRODUCER_VALUE_SERIALIZER, ListProducerSerializer.class.getName());
```

### Conclusion:

To resolve the issue, you need to ensure that the value type you are sending (in this case, `List<Producer>`) has a
corresponding serializer that can convert it to a byte array. You can either implement a custom serializer or change the
data structure to something that can be easily serialized, like a JSON string.
### What’s Happening

1. **Kafka Producer**: You are trying to send messages to a Kafka topic using a producer. Each message has a key and a
   value.

2. **Key and Value**: In your case, the key is a `String` (like "truck_id0," "truck_id1," etc.), and the value is
   a `List<Producer>`, which is a list of custom objects.

3. **Serialization**: Before sending the message, Kafka needs to convert both the key and the value into a format that
   can be transmitted over the network. This process is called serialization.

### The Problem

- You have set the key serializer to `StringSerializer`, which is correct for your string keys.
- However, for the value (which is a `List<Producer>`), you are using `ListSerializer`. This is not a standard
  serializer that Kafka recognizes, which is why you are getting the error: **"Not able to determine the serializer
  class..."**

### Solutions

To fix this issue, you have two main options:

1. **Create a Custom Serializer**:
    - You need to write a class that tells Kafka how to convert your `List<Producer>` into a byte array. This is called
      a custom serializer.
    - For example, you could convert the list into a JSON string and then serialize that string.

2. **Use a Simple Data Type**:
    - Instead of sending a `List<Producer>`, you could convert the list into a simpler format, like a JSON string,
      before sending it. This way, you can use a standard string serializer.

### Example of a Simple Solution

If you want to go with the second option (using a JSON string), here’s a simplified approach:

1. **Convert the List to JSON**:
    - Before sending the message, convert your `List<Producer>` to a JSON string.

2. **Send the JSON String**:
    - Use a `String` as the value instead of `List<Producer>`.

Here’s a rough idea of how you might do this:

```java
import com.fasterxml.jackson.databind.ObjectMapper;
@SuppressWarnings("all")

// Assuming you have a method to convert List<Producer> to JSON
public static String convertListToJson(List<Producer> producers) {
    ObjectMapper objectMapper = new ObjectMapper();
    try {
        return objectMapper.writeValueAsString(producers);
    } catch (Exception e) {
        e.printStackTrace();
        return null; // Handle this properly in production code
    }
}

// In your sendMessage method
public static void sendMessge() {
    Properties props = ProducerConfig.getKafkaProperties();
    for (int j = 0; j < 2; j++) {
        for (int i = 0; i < 100; i++) {
            String key = "truck_id" + i;
            String value = convertListToJson(producers); // Convert to JSON string
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
```

### Summary

- **Problem**: Kafka doesn't know how to serialize `List<Producer>`.
- **Solution**: Either create a custom serializer or convert the list to a JSON string and send that instead.
- **Recommendation**: The JSON string approach is simpler and avoids the need for a custom serializer.

### Clarification on Kafka Producer Types

In your original code, you were trying to send a `List<Producer>` as the value. This means you need a serializer that
can handle `List<Producer>`. If you want to send a different data type (like a JSON string or a custom object), you need
to ensure that the appropriate serializer is used.

### Options for Sending Different Data Types

1. **Using a Custom Serializer**:
    - If you want to keep using `List<Producer>`, you need to create a custom serializer that knows how to convert
      a `List<Producer>` into a byte array.
    - This serializer would typically convert the list to a JSON string (or another format) and then serialize that
      string.

2. **Using a Standard Serializer**:
    - If you convert your `List<Producer>` to a JSON string, you can use the `StringSerializer` for the value. This is a
      common approach because JSON is a widely used format for data interchange.

### Example of Custom Serializer for List<Producer>

If you want to stick with sending `List<Producer>` directly, you would need to implement a custom serializer. Here’s a
simple example of what that might look like:

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

### Update Your Producer Configuration

You would then update your producer configuration to use this custom serializer:

```bash
props.setProperty(PRODUCER_VALUE_SERIALIZER, ListProducerSerializer.class.getName());
```

### Sending the Message

With the custom serializer in place, you can continue to send `List<Producer>` as the value:

```java
ProducerRecord<String, List<Producer>> record = new ProducerRecord<>(TOPIC, key, producers);
```

### Summary

- **Kafka can handle various data types**: You can send different types of data, but you must provide the correct
  serializer for that data type.
- **Custom Serializer**: If you want to send `List<Producer>`, you need to implement a custom serializer that knows how
  to convert that list into a byte array.
- **JSON String Option**: Alternatively, you can convert the list to a JSON string and use `StringSerializer`, which is
  simpler and often sufficient.

### Step 1: Update Your Kafka Producer Configuration

You need to tell your Kafka producer to use the `ListProducerSerializer` for serializing the value of the messages.
Update your `getKafkaProperties` method to include this serializer:

```java
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

### Step 2: Sending Messages

Now that your producer is configured to use the custom serializer, you can send messages as you were doing before.
Here’s how your `sendMessage` method might look:

```java
public static void sendMessge() {
    Properties props = ProducerConfig.getKafkaProperties();
    for (int j = 0; j < 2; j++) {
        for (int i = 0; i < 100; i++) {
            String key = "truck_id" + i;
            try (KafkaProducer<String, List<Producer>> producer = new KafkaProducer<>(props)) {
                ProducerRecord<String, List<Producer>> record = new ProducerRecord<>(TOPIC, key, producers);
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

### Step 3: Testing

1. **Run Your Application**: Execute your application to see if it successfully sends messages to the Kafka topic.
2. **Check for Errors**: Monitor the logs for any errors during serialization or sending messages. If everything is set
   up correctly, you should see logs indicating that messages were sent successfully.

### Step 4: Consuming Messages (Optional)

If you want to verify that the messages are being sent correctly, you can create a Kafka consumer that reads from the
same topic and deserializes the messages. You would need to implement a corresponding deserializer for `List<Producer>`
if you want to read the messages back in their original form.

### Summary

- **Integrate the Custom Serializer**: Update your Kafka producer configuration to use the `ListProducerSerializer`.
- **Send Messages**: Use the producer to send messages as you did before.
- **Test Your Setup**: Run your application and check for successful message sending.

# Kafka Producer and Consumer with Different Data Types

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
## Sending Messages with Different Data Types

### 1. Custom Serializer for Lists

To send a list of custom objects (e.g., `List<Producer>`), you need to create a custom serializer. Below is an example
of a custom serializer that uses Jackson to convert a list of `Producer` objects to a byte array.

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

### 2. Kafka Producer Configuration

In your Kafka producer configuration, set the custom serializer for the value:

```java
public static Properties getKafkaProperties() {
    Properties props = new Properties();
    props.setProperty(BOOTSTRAP_SERVERS, HOSTNAME);
    props.setProperty(ACKS, "all");
    props.setProperty(RETRIES, Integer.toString(Integer.MAX_VALUE));
    props.setProperty(PRODUCER_KEY_SERIALIZER, StringSerializer.class.getName());
    props.setProperty(PRODUCER_VALUE_SERIALIZER, ListProducerSerializer.class.getName()); // Use custom serializer
    return props;
}
```

### 3. Sending Messages

You can now send messages with a list of `Producer` objects:

```bash
ProducerRecord<String, List<Producer>> record = new ProducerRecord<>(TOPIC, key, producers);
producer.send(record);
```

## Receiving Messages with Different Data Types

To receive messages with a custom data type, you will need to implement a corresponding deserializer.

### Custom Deserializer for Lists

Here’s an example of a custom deserializer for `List<Producer>`:

```java
import org.apache.kafka.common.serialization.Deserializer;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.Map;

public class ListProducerDeserializer implements Deserializer<List<Producer>> {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public List<Producer> deserialize(String topic, byte[] data) {
        try {
            return objectMapper.readValue(data,
                    objectMapper.getTypeFactory().constructCollectionType(List.class, Producer.class));
        } catch (Exception e) {
            throw new RuntimeException("Error deserializing List<Producer>", e);
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

### Kafka Consumer Configuration

In your Kafka consumer configuration, set the custom deserializer for the value:

```bash
props.setProperty(CONSUMER_VALUE_DESERIALIZER, ListProducerDeserializer.class.getName());
```

### 4. Consuming Messages

You can now consume messages and deserialize them into a `List<Producer>`:

```bash
ConsumerRecord<String, List<Producer>> record = consumer.poll(Duration.ofMillis(100)).iterator().next();
```

## Sending Messages with Different Data Types

### 1. Sending a Single Object

To send a single object (e.g., a `Producer` object), you need to create a custom serializer for that object.

#### Custom Serializer for Single Object

```java
import org.apache.kafka.common.serialization.Serializer;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Map;

public class ProducerSerializer implements Serializer<Producer> {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public byte[] serialize(String topic, Producer data) {
        try {
            return objectMapper.writeValueAsBytes(data);
        } catch (Exception e) {
            throw new RuntimeException("Error serializing Producer", e);
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

### 2. Sending a List of Objects

To send a list of objects (e.g., `List<Producer>`), you can use the custom serializer created earlier.

#### Custom Serializer for List of Objects

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

### 3. Kafka Producer Configuration

In your Kafka producer configuration, set the appropriate serializer for the value based on what you are sending:

```java
public static Properties getKafkaProperties() {
    Properties props = new Properties();
    props.setProperty(BOOTSTRAP_SERVERS, HOSTNAME);
    props.setProperty(ACKS, "all");
    props.setProperty(RETRIES, Integer.toString(Integer.MAX_VALUE));
    props.setProperty(PRODUCER_KEY_SERIALIZER, StringSerializer.class.getName());
    // Use the appropriate serializer based on the data type
    props.setProperty(PRODUCER_VALUE_SERIALIZER, ListProducerSerializer.class.getName()); // For List<Producer>
    // props.setProperty(PRODUCER_VALUE_SERIALIZER, ProducerSerializer.class.getName()); // For single Producer
    return props;
}
```

### 4. Sending Messages

You can now send messages with either a single `Producer` object or a list of `Producer` objects:

```bash
// Sending a single Producer object
ProducerRecord<String, Producer> singleRecord = new ProducerRecord<>(TOPIC, key, singleProducer);
producer.send(singleRecord);

// Sending a list of Producer objects
ProducerRecord<String, List<Producer>> listRecord = new ProducerRecord<>(TOPIC, key, producers);
producer.send(listRecord);
```

## Receiving Messages with Different Data Types

To receive messages with custom data types, you will need to implement corresponding deserializers.

### 1. Custom

# Kafka Producer and Consumer with Different Data Types

This guide explains how to use Apache Kafka to send and receive messages with various data types, including custom
objects like `Product`, `Order`, and `Customer`.

## Overview

In this example, we will demonstrate how to:

- Send a single custom object as a message.
- Send a list of custom objects as a message.
- Use different key types for messages.

## Custom Classes

### 1. Define Your Custom Classes

You will need to define the custom classes that you want to send as messages. Here are examples of `Product`, `Order`,
and `Customer` classes:

```java
public class Product {
    private String id;
    private String name;
    private double price;

    // Getters and Setters
}

public class Order {
    private String orderId;
    private String customerId;
    private List<Product> products;

    // Getters and Setters
}

public class Customer {
    private String customerId;
    private String name;
    private String email;

    // Getters and Setters
}
```

## Sending Messages with Different Data Types

### 2. Custom Serializers

You will need to create custom serializers for each of your custom classes.

#### Serializer for Product

```java
import org.apache.kafka.common.serialization.Serializer;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Map;

public class ProductSerializer implements Serializer<Product> {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public byte[] serialize(String topic, Product data) {
        try {
            return objectMapper.writeValueAsBytes(data);
        } catch (Exception e) {
            throw new RuntimeException("Error serializing Product", e);
        }
    }

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {}

    @Override
    public void close() {}
}
```

#### Serializer for Order

```java
public class OrderSerializer implements Serializer<Order> {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public byte[] serialize(String topic, Order data) {
        try {
            return objectMapper.writeValueAsBytes(data);
        } catch (Exception e) {
            throw new RuntimeException("Error serializing Order", e);
        }
    }

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {}

    @Override
    public void close() {}
}
```

#### Serializer for Customer

```java
public class CustomerSerializer implements Serializer<Customer> {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public byte[] serialize(String topic, Customer data) {
        try {
            return objectMapper.writeValueAsBytes(data);
        } catch (Exception e) {
            throw new RuntimeException("Error serializing Customer", e);
        }
    }

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {}

    @Override
    public void close() {}
}
```

### 3. Kafka Producer Configuration

In your Kafka producer configuration, set the appropriate serializer for the value based on what you are sending. You
can also set the key type to something like `String` or `Integer`.

```java
public static Properties getKafkaProperties() {
    Properties props = new Properties();
    props.setProperty(BOOTSTRAP_SERVERS, HOSTNAME);
    props.setProperty(ACKS, "all");
    props.setProperty(RETRIES, Integer.toString(Integer.MAX_VALUE));
    props.setProperty(PRODUCER_KEY_SERIALIZER, StringSerializer.class.getName()); // Key type can be String or Integer
    // Use the appropriate serializer based on the data type
    props.setProperty(PRODUCER_VALUE_SERIALIZER, ProductSerializer.class.getName()); // For Product
    // props.setProperty(PRODUCER_VALUE_SERIALIZER, OrderSerializer.class.getName()); // For Order
    // props.setProperty(PRODUCER_VALUE_SERIALIZER, CustomerSerializer.class.getName()); // For Customer
    return props;
}
```

### 4. Sending Messages

You can now send messages with either a single `Product`, `Order`, or `Customer` object:

```bash
// Sending a single Product object
ProducerRecord<String, Product> productRecord = new ProducerRecord<>(TOPIC, "productKey", product);
producer.send(productRecord);

// Sending a single Order object
ProducerRecord<String, Order> orderRecord = new ProducerRecord<>(TOPIC, "orderKey", order);
producer.send(orderRecord);

// Sending a single Customer object
ProducerRecord<String, Customer> customerRecord = new ProducerRecord<>(TOPIC, "customerKey", customer);
producer.send(customerRecord);
```

## Receiving Messages with Different Data Types

## Receiving Messages with Different Data Types

To receive messages with custom data types, you will need to implement corresponding deserializers for each of your
custom classes.

### 5. Custom Deserializers

You will need to create custom deserializers for each of your custom classes.

#### Deserializer for Product

```java
import org.apache.kafka.common.serialization.Deserializer;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Map;

public class ProductDeserializer implements Deserializer<Product> {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public Product deserialize(String topic, byte[] data) {
        try {
            return objectMapper.readValue(data, Product.class);
        } catch (Exception e) {
            throw new RuntimeException("Error deserializing Product", e);
        }
    }

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {}

    @Override
    public void close() {}
}
```

#### Deserializer for Order

```java
public class OrderDeserializer implements Deserializer<Order> {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public Order deserialize(String topic, byte[] data) {
        try {
            return objectMapper.readValue(data, Order.class);
        } catch (Exception e) {
            throw new RuntimeException("Error deserializing Order", e);
        }
    }

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {}

    @Override
    public void close() {}
}
```

#### Deserializer for Customer

```java
public class CustomerDeserializer implements Deserializer<Customer> {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public Customer deserialize(String topic, byte[] data) {
        try {
            return objectMapper.readValue(data, Customer.class);
        } catch (Exception e) {
            throw new RuntimeException("Error deserializing Customer", e);
        }
    }

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {}

    @Override
    public void close() {}
}
```

### 6. Kafka Consumer Configuration

In your Kafka consumer configuration, set the appropriate deserializer for the value based on what you are receiving.
You can also set the key type to match what you used when sending messages.

```java
public static Properties getConsumerProperties() {
    Properties props = new Properties();
    props.setProperty(BOOTSTRAP_SERVERS, HOSTNAME);
    props.setProperty(GROUP_ID_CONFIG, "your-consumer-group");
    props.setProperty(AUTO_OFFSET_RESET_CONFIG, "earliest");
    props.setProperty(ENABLE_AUTO_COMMIT_CONFIG, "true");
    props.setProperty(PRODUCER_KEY_DESERIALIZER,
            StringDeserializer.class.getName()); // Key type can be String or Integer
    // Use the appropriate deserializer based on the data type
    props.setProperty(PRODUCER_VALUE_DESERIALIZER, ProductDeserializer.class.getName()); // For Product
     props.setProperty(PRODUCER_VALUE_DESERIALIZER, OrderDeserializer.class.getName()); // For Order
     props.setProperty(PRODUCER_VALUE_DESERIALIZER, CustomerDeserializer.class.getName()); // For Customer
    return props;
}
```

### 7. Consuming Messages

You can now consume messages and deserialize them into `Product`, `Order`, or `Customer` objects:

```bash
KafkaConsumer<String, Product> consumer = new KafkaConsumer<>(getConsumerProperties());
consumer.subscribe(Collections.singletonList(TOPIC));

while (true) {
    ConsumerRecords<String, Product> records = consumer.poll(Duration.ofMillis(100));
    for (ConsumerRecord<String, Product> record : records) {
        System.out.printf("Consumed Product: Key = %s, Value = %s%n", record.key(), record.value());
    }
}

// Similarly, you can consume Order and Customer messages
```

## Summary

In this guide, you learned how to:

- Define custom classes (`Product`, `Order`, `Customer`).
- Create custom serializers and deserializers for these classes.
- Configure Kafka producers and consumers to send and receive messages with different data types.
- Send and receive messages using various key types.

By following these steps, you can effectively use Apache Kafka to handle complex data types beyond just strings.

## Additional Resources

- [Apache Kafka Documentation](https://kafka.apache.org/documentation/)
- [Jackson Databind Documentation](https://github.com/FasterXML/jackson-databind)

Feel free to modify the examples to fit your specific use case!


# Kafka Producer and Consumer with Different Data Types

This guide explains how to use Apache Kafka to send and receive messages with various data types, including custom objects like `Product`, `Order`, and `Customer`, without using `String` for keys.

## Overview

In this example, we will demonstrate how to:

- Send a single custom object as a message.
- Send a list of custom objects as a message.
- Use different key types for messages, such as `Integer` or custom objects.

## Custom Classes

### 1. Define Your Custom Classes

You will need to define the custom classes that you want to send as messages. Here are examples of `Product`, `Order`, and `Customer` classes:

```java
public class Product {
    private String id;
    private String name;
    private double price;

    // Getters and Setters
}

public class Order {
    private String orderId;
    private String customerId;
    private List<Product> products;

    // Getters and Setters
}

public class Customer {
    private String customerId;
    private String name;
    private String email;

    // Getters and Setters
}
```

## Sending Messages with Different Data Types

### 2. Custom Serializers

You will need to create custom serializers for each of your custom classes.

#### Serializer for Product

```java
import org.apache.kafka.common.serialization.Serializer;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Map;

public class ProductSerializer implements Serializer<Product> {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public byte[] serialize(String topic, Product data) {
        try {
            return objectMapper.writeValueAsBytes(data);
        } catch (Exception e) {
            throw new RuntimeException("Error serializing Product", e);
        }
    }

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {}

    @Override
    public void close() {}
}
```

### 3. Kafka Producer Configuration

In your Kafka producer configuration, set the appropriate serializer for the key and value based on what you are
sending. For example, if you want to use `Integer` as the key type:

```java
public static Properties getKafkaProperties() {
    Properties props = new Properties();
    props.setProperty(BOOTSTRAP_SERVERS, HOSTNAME);
    props.setProperty(ACKS, "all");
    props.setProperty(RETRIES, Integer.toString(Integer.MAX_VALUE));
    props.setProperty(PRODUCER_KEY_SERIALIZER, IntegerSerializer.class.getName()); // Use Integer as key type
    props.setProperty(PRODUCER_VALUE_SERIALIZER, ProductSerializer.class.getName()); // For Product
    // props.setProperty(PRODUCER_VALUE_SERIALIZER, OrderSerializer.class.getName()); // For Order
    // props.setProperty(PRODUCER_VALUE_SERIALIZER, CustomerSerializer.class.getName()); // For Customer
    return props;
}
```

### 4. Sending Messages

You can now send messages with either a single `Product`, `Order`, or `Customer` object, using an `Integer` key:

```bash
// Sending a single Product object with Integer key
ProducerRecord<Integer, Product> productRecord = new ProducerRecord<>(TOPIC, 1, product);
producer.send(productRecord);

// Sending a single Order object with Integer key
ProducerRecord<Integer, Order> orderRecord = new ProducerRecord<>(TOPIC, 2, order);
producer.send(orderRecord);

// Sending a single Customer object with Integer key
ProducerRecord<Integer, Customer> customerRecord = new ProducerRecord<>(TOPIC, 3, customer);
producer.send(customerRecord);
```

## Receiving Messages with Different Data Types

To receive messages with custom data types, you will need to implement corresponding deserializers.

### 5. Custom Deserializers

You will need to create custom deserializers for each of your custom classes.

#### Deserializer for Product

```java
import org.apache.kafka.common.serialization.Deserializer;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Map;

public class ProductDeserializer implements Deserializer<Product> {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public Product deserialize(String topic, byte[] data) {
        try {
            return objectMapper.readValue(data, Product.class);
        } catch (Exception e) {
            throw new RuntimeException("Error deserializing Product", e);
        }
    }

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {}

    @Override
    public void close() {}
}
```

# Kafka Producer and Consumer with Different Data Types

This guide explains how to use Apache Kafka to send and receive messages with various data types, including custom
objects like `Product`, `Order`, and `Customer`, using different key types for each message.

## Overview

In this example, we will demonstrate how to:

- Send a single custom object as a message with different key types.
- Use custom serializers and deserializers for each key type.

## Custom Classes

### 1. Define Your Custom Classes

You will need to define the custom classes that you want to send as messages. Here are examples of `Product`, `Order`,
and `Customer` classes:

```java
public class Product {
    private String id;
    private String name;
    private double price;

    // Getters and Setters
}

public class Order {
    private String orderId;
    private String customerId;
    private List<Product> products;

    // Getters and Setters
}

public class Customer {
    private String customerId;
    private String name;
    private String email;

    // Getters and Setters
}
```

## Sending Messages with Different Data Types

### 2. Custom Serializers

You will need to create custom serializers for each of your custom classes.

#### Serializer for Product

```java
import org.apache.kafka.common.serialization.Serializer;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Map;

public class ProductSerializer implements Serializer<Product> {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public byte[] serialize(String topic, Product data) {
        try {
            return objectMapper.writeValueAsBytes(data);
        } catch (Exception e) {
            throw new RuntimeException("Error serializing Product", e);
        }
    }

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {}

    @Override
    public void close() {}
}
```

#### Serializer for Order

```java
public class OrderSerializer implements Serializer<Order> {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public byte[] serialize(String topic, Order data) {
        try {
            return objectMapper.writeValueAsBytes(data);
        } catch (Exception e) {
            throw new RuntimeException("Error serializing Order", e);
        }
    }

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {}

    @Override
    public void close() {}
}
```

#### Serializer for Customer

```java
public class CustomerSerializer implements Serializer<Customer> {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public byte[] serialize(String topic, Customer data) {
        try {
            return objectMapper.writeValueAsBytes(data);
        } catch (Exception e) {
            throw new RuntimeException("Error serializing Customer", e);
        }
    }

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {}

    @Override
    public void close() {}
}
```

### 3. Kafka Producer Configuration

In your Kafka producer configuration, set the appropriate serializer for the value based on what you are sending. You
can also set the key type to match the custom object you are using.

```java
public static Properties getKafkaProperties() {
    Properties props = new Properties();
    props.setProperty(BOOTSTRAP_SERVERS, HOSTNAME);
    props.setProperty(ACKS, "all");
    props.setProperty(RETRIES, Integer.toString(Integer.MAX_VALUE));
    // Use the appropriate serializer based on the data type,
    // For example, if sending a Product with a Product key
    props.setProperty(PRODUCER_KEY_SERIALIZER, ProductSerializer.class.getName()); // Key type as Product
    props.setProperty(PRODUCER_VALUE_SERIALIZER, ProductSerializer.class.getName()); // For Product
    // props.setProperty(PRODUCER_KEY_SERIALIZER, OrderSerializer.class.getName()); // Key type as Order
    // props.setProperty(PRODUCER_VALUE_SERIALIZER, OrderSerializer.class.getName()); // For Order
    // props.setProperty(PRODUCER_KEY_SERIALIZER, CustomerSerializer.class.getName()); // Key type as Customer
    // props.setProperty(PRODUCER_VALUE_SERIALIZER, CustomerSerializer.class.getName()); // For Customer
    return props;
}
```

### 4. Sending Messages

You can now send messages with different key types:

```bash
// Sending a Product object with Product as key
Product productKey = new Product(); // Create a Product object for the key
productKey.setId("P1");
productKey.setName("Product 1");


productKey.setPrice(100.0);

Product productValue = new Product(); // Create a Product object for the value
productValue.setId("P1");
productValue.setName("Product 1");
productValue.setPrice(100.0);

ProducerRecord<Product, Product> productRecord = new ProducerRecord<>(TOPIC, productKey, productValue);
producer.send(productRecord);

// Sending an Order object with Order as key
Order orderKey = new Order(); // Create an Order object for the key
orderKey.setOrderId("O1");
orderKey.setCustomerId("C1");
orderKey.setProducts(Arrays.asList(productValue)); // Add products to the order

Order orderValue = new Order(); // Create an Order object for the value
orderValue.setOrderId("O1");
orderValue.setCustomerId("C1");
orderValue.setProducts(Arrays.asList(productValue));

ProducerRecord<Order, Order> orderRecord = new ProducerRecord<>(TOPIC, orderKey, orderValue);
producer.send(orderRecord);

// Sending a Customer object with Customer as key
Customer customerKey = new Customer(); // Create a Customer object for the key
customerKey.setCustomerId("C1");
customerKey.setName("Customer 1");
customerKey.setEmail("customer1@example.com");

Customer customerValue = new Customer(); // Create a Customer object for the value
customerValue.setCustomerId("C1");
customerValue.setName("Customer 1");
customerValue.setEmail("customer1@example.com");

ProducerRecord<Customer, Customer> customerRecord = new ProducerRecord<>(TOPIC, customerKey, customerValue);
producer.send(customerRecord);
```

## Receiving Messages with Different Data Types

To receive messages with custom data types, you will need to implement corresponding deserializers for each key and
value type.

### 5. Custom Deserializers

You will need to create custom deserializers for each of your custom classes.

#### Deserializer for Product

```java
import org.apache.kafka.common.serialization.Deserializer;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Map;

public class ProductDeserializer implements Deserializer<Product> {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public Product deserialize(String topic, byte[] data) {
        try {
            return objectMapper.readValue(data, Product.class);
        } catch (Exception e) {
            throw new RuntimeException("Error deserializing Product", e);
        }
    }

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {}

    @Override
    public void close() {}
}
```

#### Deserializer for Order

```java
public class OrderDeserializer implements Deserializer<Order> {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public Order deserialize(String topic, byte[] data) {
        try {
            return objectMapper.readValue(data, Order.class);
        } catch (Exception e) {
            throw new RuntimeException("Error deserializing Order", e);
        }
    }

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {}

    @Override
    public void close() {}
}
```

#### Deserializer for Customer

```java
public class CustomerDeserializer implements Deserializer<Customer> {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public Customer deserialize(String topic, byte[] data) {
        try {
            return objectMapper.readValue(data, Customer.class);
        } catch (Exception e) {
            throw new RuntimeException("Error deserializing Customer", e);
        }
    }

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {}

    @Override
    public void close() {}
}
```

### 6. Kafka Consumer Configuration

In your Kafka consumer configuration, set the appropriate deserializer for both the key and value based on what you are
receiving.

```bash
public static Properties getConsumerProperties() {
    Properties props = new Properties();
    props.setProperty(BOOTSTRAP_SERVERS, HOSTNAME);
    props.setProperty(GROUP_ID_CONFIG, "your-consumer-group");
    props.setProperty(AUTO_OFFSET_RESET_CONFIG, "earliest");
    props.setProperty(ENABLE_AUTO_COMMIT_CONFIG, "true");
    
    // Use the appropriate deserializer based on the data type
    props.setProperty(CONSUMER_KEY_DESERIALIZER, ProductDeserializer.class.getName()); // For Product key
    props.setProperty(CONSUMER_VALUE_DESERIALIZER, ProductDeserializer.class.getName()); // For Product value
    // props.setProperty(CONSUMER_KEY_DESERIALIZER, OrderDeserializer.class.getName()); // For Order key
    // props.setProperty(CONSUMER_VALUE_DESERIALIZER, OrderDeserializer.class.getName()); // For Order value
    //

    // props.setProperty(CONSUMER_KEY_DESERIALIZER, CustomerDeserializer.class.getName()); // For Customer key
    // props.setProperty(CONSUMER_VALUE_DESERIALIZER, CustomerDeserializer.class.getName()); // For Customer value
    return props;
}
```

### 7. Consuming Messages

You can now consume messages and deserialize them into `Product`, `Order`, or `Customer` objects. Here’s how you can set
up a consumer for each type:

#### Consuming Product Messages

```bash
KafkaConsumer<Product, Product> productConsumer = new KafkaConsumer<>(getConsumerProperties());
productConsumer.subscribe(Collections.singletonList(TOPIC));

while (true) {
    ConsumerRecords<Product, Product> records = productConsumer.poll(Duration.ofMillis(100));
    for (ConsumerRecord<Product, Product> record : records) {
        System.out.printf("Consumed Product: Key = %s, Value = %s%n", record.key(), record.value());
    }
}
```

#### Consuming Order Messages

```bash
KafkaConsumer<Order, Order> orderConsumer = new KafkaConsumer<>(getConsumerProperties());
orderConsumer.subscribe(Collections.singletonList(TOPIC));

while (true) {
    ConsumerRecords<Order, Order> records = orderConsumer.poll(Duration.ofMillis(100));
    for (ConsumerRecord<Order, Order> record : records) {
        System.out.printf("Consumed Order: Key = %s, Value = %s%n", record.key(), record.value());
    }
}
```

#### Consuming Customer Messages

```bash
KafkaConsumer<Customer, Customer> customerConsumer = new KafkaConsumer<>(getConsumerProperties());
customerConsumer.subscribe(Collections.singletonList(TOPIC));

while (true) {
    ConsumerRecords<Customer, Customer> records = customerConsumer.poll(Duration.ofMillis(100));
    for (ConsumerRecord<Customer, Customer> record : records) {
        System.out.printf("Consumed Customer: Key = %s, Value = %s%n", record.key(), record.value());
    }
}
```

## Summary

In this guide, you learned how to:

- Define custom classes (`Product`, `Order`, `Customer`).
- Create custom serializers and deserializers for these classes.
- Configure Kafka producers and consumers to send and receive messages with different data types.
- Use different key types for messages, such as `Product`, `Order`, and `Customer`.

By following these steps, you can effectively use Apache Kafka to handle complex data types and different key types in
your messaging system.

## Additional Resources

- [Apache Kafka Documentation](https://kafka.apache.org/documentation/)
- [Jackson Databind Documentation](https://github.com/FasterXML/jackson-databind)

### Advantages of Using Gson

1. **Simplicity**: Gson has a straightforward API that is straightforward to use for converting Java objects to JSON and back.
2. **Lightweight**: Gson is generally smaller compared to Jackson, which can be beneficial if you're looking to
   minimize dependencies.
3. **Flexibility**: Gson can handle a wide variety of Java types, including collections and nested objects.

### Example of Using Gson for Serialization and Deserialization

Here’s how you can modify the custom serializers and deserializers to use Gson instead of Jackson:

#### Custom Serializer Using Gson

```java
import com.google.gson.Gson;
import org.apache.kafka.common.serialization.Serializer;

import java.util.Map;

public class ProductSerializer implements Serializer<Product> {
    private final Gson gson = new Gson();

    @Override
    public byte[] serialize(String topic, Product data) {
        return gson.toJson(data).getBytes(); // Convert to JSON and then to byte array
    }

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {}

    @Override
    public void close() {}
}
```

#### Custom Deserializer Using Gson

```java
import com.google.gson.Gson;
import org.apache.kafka.common.serialization.Deserializer;

import java.util.Map;

public class ProductDeserializer implements Deserializer<Product> {
    private final Gson gson = new Gson();

    @Override
    public Product deserialize(String topic, byte[] data) {
        return gson.fromJson(new String(data), Product.class); // Convert a byte array to JSON string and then to object
    }

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {}

    @Override
    public void close() {}
}
```

### Summary

Using Gson for serialization and deserialization in your Kafka producer and consumer is a valid and often effective
choice. It can simplify your code and reduce the number of dependencies.