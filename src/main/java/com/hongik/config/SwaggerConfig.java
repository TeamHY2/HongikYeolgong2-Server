package com.hongik.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        Info info = new Info()
                .version("v1.0")
                .title("홍익열공이")
                .description("열람실 이용 및 공부 시간과 횟수를 조회");

        return new OpenAPI()
                .info(info);
    }
}
