package com.zvonimir.imageconverter.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // Allow all paths
                .allowedOrigins("*") // Allow all origins (for dev)
                .allowedMethods("*") // Allow all HTTP methods
                .allowedHeaders("*");
    }
}
