package co.botechservices.novlnovl;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableWebSecurity
@SpringBootApplication
@EnableTransactionManagement
public class StepBooksAPIApplication {

    public static void main(String[] args) {
        SpringApplication.run(StepBooksAPIApplication.class, args);
    }

}
