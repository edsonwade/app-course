package code.with.vanilson.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

/**
 * SecurityConfig
 *
 * @author vamuhong
 * @version 1.0
 * @since 2024-12-03
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable) // Disable CSRF for simplicity
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/welcome").permitAll() // Permit all access to /auth/welcome
                        .requestMatchers("/api/registrations/create")
                        .authenticated() // Require authentication for /api/registrations/create
                        .anyRequest().authenticated() // Require authentication for all other requests
                )
                .httpBasic(withDefaults()) // Enable Basic Authentication
                .formLogin(withDefaults()); // Enable form-based login (optional)

        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        // This is a simple in-memory user authentication for testing
        return new InMemoryUserDetailsManager(
                User.withUsername("vani")
                        .password("{noop}123") // No encoding for simplicity in this example
                        .roles("USER")
                        .build()
        );
    }
}
