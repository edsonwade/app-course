package code.with.vanilson.product;

import java.math.BigDecimal;

public record Product(String name, int quantiy, BigDecimal price, String version) {
}
