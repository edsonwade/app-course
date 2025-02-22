package code.with.vanilson.util;

/**
 * PropertyUtil
 *
 * @author vamuhong
 * @version 1.0
 * @since 2025-02-22
 */
@SuppressWarnings("all")
public class PropertyUtil {

    public static final String BOOTSTRAP_SERVERS = "bootstrap.servers";
    public static final String HOSTNAME = "localhost:9092";
    public static final String TOPIC = "twitter-topic";
    public static final String ACKS = "acks";
    public static final String PRODUCER_KEY_SERIALIZER = "key.serializer";
    public static final String PRODUCER_VALUE_SERIALIZER = "value.serializer";
    public static final String RETRIES = "retries";
    public static final String CONSUMER_KEY_DESERIALIZER = "key.deserializer";
    public static final String CONSUMER_VALUE_DESERIALIZER = "value.deserializer";
    public static final String GROUP_ID = "group.id";
    public static final String AUTO_OFFSET_RESET = "auto.offset.reset";

    private PropertyUtil() {
        throw new AssertionError("This class cannot be instantiated");
    }
}