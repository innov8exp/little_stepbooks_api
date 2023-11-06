package net.stepbooks.infrastructure.security.user;

import lombok.RequiredArgsConstructor;
import net.stepbooks.infrastructure.security.EntryPointUnauthorizedHandler;
import net.stepbooks.infrastructure.security.RestAccessDeniedHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;

@Order(1)
@Configuration
@RequiredArgsConstructor
public class UserSecurityConfig {

    private final UserAuthFilter userAuthFilter;
    private final EntryPointUnauthorizedHandler entryPointUnauthorizedHandler;
    private final RestAccessDeniedHandler restAccessDeniedHandler;
    private final MvcRequestMatcher.Builder mvc;

    // CHECKSTYLE:OFF
    @Bean
    public SecurityFilterChain userSecurityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement((sessionManagement) ->
                        sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(userAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling((exceptions) -> exceptions
                        .authenticationEntryPoint(entryPointUnauthorizedHandler)
                        .accessDeniedHandler(restAccessDeniedHandler))
                .authorizeHttpRequests((requests) -> requests.requestMatchers(
                                mvc.pattern("/auth/**"),
                                mvc.pattern("/actuator/**"),
                                mvc.pattern("/swagger-ui/**"),
                                mvc.pattern("/v3/api-docs/**"),
                                mvc.pattern("/swagger-ui.html"))
                        .permitAll()
                        .anyRequest()
                        .authenticated()
                );
        return http.build();
    }
}