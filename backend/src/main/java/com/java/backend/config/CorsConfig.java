package com.java.backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class CorsConfig {

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();

        // Cho phép tất cả các origin (trong môi trường development)
        config.addAllowedOrigin("http://localhost:3000");

        // Cho phép tất cả các header
        config.addAllowedHeader("*");

        // Cho phép tất cả các method (GET, POST, PUT, DELETE, etc.)
        config.addAllowedMethod("*");

        // Cho phép gửi cookie (nếu cần)
        config.setAllowCredentials(true);

        source.registerCorsConfiguration("/api/**", config);
        return new CorsFilter(source);
    }
}