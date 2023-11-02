package net.stepbooks.infrastructure.config;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@SecurityScheme(
        name = "Client Authentication",
        type = SecuritySchemeType.HTTP,
        in = SecuritySchemeIn.HEADER,
        bearerFormat = "JWT",
        scheme = "bearer"
)
@SecurityScheme(
        name = "Admin Authentication",
        type = SecuritySchemeType.HTTP,
        in = SecuritySchemeIn.COOKIE,
        bearerFormat = "JWT",
        scheme = "bearer"
)
@Configuration
public class OpenAPIConfig {

    @Bean
    public OpenAPI springShopOpenAPI() {
        return new OpenAPI()
                .info(new Info().title("Stepbooks API")
                        .description("Stepbooks API reference for developers, including public APIs and Admin APIs.")
                        .version("v0.0.1"));
    }


    @Bean
    public GroupedOpenApi clientApi() {
        return GroupedOpenApi.builder()
                .group("stepbooks-client")
                .pathsToMatch("/**")
                .pathsToExclude("/admin/**")
                .addOpenApiCustomizer(clientGroupOpenApiCustomizer())
                .build();
    }

    OpenApiCustomizer clientGroupOpenApiCustomizer() {
        return openApi -> openApi.getPaths().entrySet().removeIf(path -> path.getValue().readOperations().stream()
                .anyMatch(operation -> operation.getSecurity() != null && operation.getSecurity().stream().noneMatch(
                        securityRequirement -> securityRequirement.get("Client Authentication") != null)
                ));
    }

    @Bean
    public GroupedOpenApi adminApi() {
        return GroupedOpenApi.builder()
                .group("stepbooks-admin")
                .pathsToMatch("/admin/**")
                .addOpenApiCustomizer(adminGroupOpenApiCustomizer())
                .build();
    }

    OpenApiCustomizer adminGroupOpenApiCustomizer() {
        return openApi -> openApi.getPaths().entrySet().removeIf(path -> path.getValue().readOperations().stream()
                .anyMatch(operation -> operation.getSecurity() != null && operation.getSecurity().stream().noneMatch(
                        securityRequirement -> securityRequirement.get("Admin Authentication") != null)
                ));
    }
}
