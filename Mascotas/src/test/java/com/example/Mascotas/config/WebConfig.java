package com.example.Mascotas.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // Permitir CORS en todos los endpoints (/**)
        registry.addMapping("/**")
                // Permitimos cualquier origen (Localhost y el futuro link de Netlify)
                .allowedOriginPatterns("*")
                // Permitimos los métodos necesarios
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                // Permitimos cualquier cabecera
                .allowedHeaders("*")
                // Permitir envío de credenciales (cookies, auth headers)
                .allowCredentials(true);
    }
}