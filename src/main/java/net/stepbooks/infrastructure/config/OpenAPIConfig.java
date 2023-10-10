package net.stepbooks.infrastructure.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAPIConfig {

    @Bean
    public OpenAPI springShopOpenAPI() {
        return new OpenAPI()
                .info(new Info().title("Stepbook API")
                        .description("Stepbook API reference for developers, including public APIs and Admin APIs.")
                        .version("v0.0.1"));
    }

    @Bean
    public GroupedOpenApi clientApi() {
        return GroupedOpenApi.builder()
                .group("stepbook-client")
                .pathsToMatch("/**")
                .pathsToExclude("/admin/**")
                .build();
    }
    @Bean
    public GroupedOpenApi adminApi() {
        return GroupedOpenApi.builder()
                .group("stepbook-admin")
                .pathsToMatch("/admin/**")
                .build();
    }
}
