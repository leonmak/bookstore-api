package com.example.bookstore.configuration;

import com.example.bookstore.interceptor.TracingHandlerInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class TracingConfig {

    @Bean
    public WebMvcConfigurer webMvcConfigurer(TracingHandlerInterceptor tracingInterceptor) {
        return new WebMvcConfigurer() {
            @Override
            public void addInterceptors(InterceptorRegistry registry) {
                registry.addInterceptor(tracingInterceptor)
                        .addPathPatterns("/**")  // Intercepts all requests
                        .excludePathPatterns("/login", "/register"); // Exclude specific paths
            }
        };
    }
}
