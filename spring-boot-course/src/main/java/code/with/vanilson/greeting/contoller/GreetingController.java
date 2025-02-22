package code.with.vanilson.greeting.contoller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Map;

/**
 * GreetingService
 *
 * @author vamuhong
 * @version 1.0
 * @since 2024-12-13
 */
@Controller
public class GreetingController {

    @GetMapping("greeting")
    public String greeting(Map<String, Object> model) {
        model.put("message", "Hello, Vanilson!");
        return "greeting";
    }
}