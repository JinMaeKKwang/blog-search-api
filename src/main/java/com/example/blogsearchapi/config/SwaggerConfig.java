package com.example.blogsearchapi.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springdoc.core.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public GroupedOpenApi searchOpenApi() {
        return GroupedOpenApi.builder()
                .group("blog-search")
                .pathsToMatch("/api/**")
                .build();
    }

    @Bean
    public OpenAPI customOpenApi() {
        return new OpenAPI()
                .components(new Components())
                .info(new Info().title("Blog-Search-API")
                        .description("카카오 및 네이버의 OPEN API를 활용하여 블로그를 검색합니다.")
                        .version("v0.0.1"));
    }
}
