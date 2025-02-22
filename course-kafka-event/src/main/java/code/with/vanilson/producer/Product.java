package code.with.vanilson.producer;

import java.math.BigDecimal;

public record Product(String name, int quantiy, BigDecimal price, String version) {
}
