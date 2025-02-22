package code.with.vanilson;

import code.with.vanilson.event.KafkaEventProducer;

/**
 * Hello world!
 */
public class KafkaEventStreamApp {
    public static void main(String[] args) {
        //Product sent a message
        KafkaEventProducer.sendMessge();
    }
}
