package code.with.vanilson.mapper;

import code.with.vanilson.producer.Product;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.header.Headers;
import org.apache.kafka.common.serialization.Serializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

public class ListProducerSerializer implements Serializer<List<Product>> {
    private static final Logger log = LoggerFactory.getLogger(ListProducerSerializer.class);

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
        Serializer.super.configure(configs, isKey);
    }

    @Override
    public byte[] serialize(String topic, List<Product> data) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            log.info("Object converted to arrays of bytes success");
            return mapper.writeValueAsBytes(data);

        } catch (JsonProcessingException e) {
            log.error("Couldn't serializa object:{}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public byte[] serialize(String topic, Headers headers, List<Product> data) {
        return Serializer.super.serialize(topic, headers, data);
    }

    @Override
    public void close() {
        Serializer.super.close();
    }
}
