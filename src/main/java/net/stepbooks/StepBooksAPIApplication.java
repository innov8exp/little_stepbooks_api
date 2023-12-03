package net.stepbooks;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.TimeZone;

@EnableAsync
@EnableScheduling
@EnableFeignClients
@EnableWebSecurity
@SpringBootApplication
@EnableTransactionManagement
public class StepBooksAPIApplication {

    public static void main(String[] args) {
        TimeZone timeZone = TimeZone.getTimeZone("UTC");
        TimeZone.setDefault(timeZone);
        SpringApplication.run(StepBooksAPIApplication.class, args);
    }

}
