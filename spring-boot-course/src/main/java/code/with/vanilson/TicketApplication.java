package code.with.vanilson;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;


/**
 * TicketApplication
 *
 * @author vamuhong
 * @version 1.0
 * @since 2024-12-02
 */
@SpringBootApplication(scanBasePackages = "code.with.vanilson.jsp")
public class TicketApplication extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(TicketApplication.class);
    }


    public static void main(String... args) {
        SpringApplication.run(TicketApplication.class,args);
    }
}