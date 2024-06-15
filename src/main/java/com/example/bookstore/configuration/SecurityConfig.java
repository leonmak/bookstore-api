package com.example.bookstore.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, jsr250Enabled = true)
public class SecurityConfig extends GlobalMethodSecurityConfiguration {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeHttpRequests((requests) -> requests
                        .requestMatchers("/v3/**", "/swagger-ui/**", "/api/**").permitAll()
                        .requestMatchers(HttpMethod.DELETE, "/api/books/*").hasRole("ADMIN")
                )
                .csrf(c -> c.disable())
                .build();
    }

    @Bean
    public UserDetailsService userDetailsService(
            @Value("${admin.username:admin}") String adminUser,
            @Value("${admin.password:admin}") String adminPass
    ) {
        UserDetails user = User.withDefaultPasswordEncoder()
                .username(adminUser)
                .password(adminPass)
                .roles("ADMIN")
                .build();
        return new InMemoryUserDetailsManager(user);
    }
}
