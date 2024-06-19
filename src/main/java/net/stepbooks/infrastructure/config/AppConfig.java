package net.stepbooks.infrastructure.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
public class AppConfig {

    @Value("${stepbooks.mock:0}")
    private boolean mock;

    @Value("${stepbooks.admin-email:0}")
    private String adminEmail;
}
