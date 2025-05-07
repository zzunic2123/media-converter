package com.zvonimir.imageconverter.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI imageConverterOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("🖼️ Image Converter API")
                        .version("1.0")
                        .description("This API allows users to upload an image and convert it to another format such as PNG, JPEG, BMP, or GIF."));
    }
}
