package net.stepbooks.infrastructure.security.admin;

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

@Order(2)
@Configuration
@RequiredArgsConstructor
public class AdminSecurityConfig {

    private final EntryPointUnauthorizedHandler entryPointUnauthorizedHandler;
    private final RestAccessDeniedHandler restAccessDeniedHandler;
    private final MvcRequestMatcher.Builder mvc;
    private final AdminAuthFilter adminAuthFilter;

    // CHECKSTYLE:OFF
    @Bean
    public SecurityFilterChain adminSecurityFilterChain(HttpSecurity http) throws Exception {
        http
                .securityMatcher("/admin/**")
//                .csrf(csrf -> csrf.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
//                        .ignoringRequestMatchers(
//                                mvc.pattern("/admin/auth/login"),
//                                mvc.pattern("/admin/auth/refresh"),
//                                mvc.pattern("/admin/auth/logout")))
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement((sessionManagement) -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(adminAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling((exceptions) -> exceptions
                        .authenticationEntryPoint(entryPointUnauthorizedHandler)
                        .accessDeniedHandler(restAccessDeniedHandler))
                .authorizeHttpRequests((requests) -> requests.requestMatchers(
                                mvc.pattern("/admin/auth/login"),
                                mvc.pattern("/admin/auth/refresh"),
                                mvc.pattern("/admin/auth/logout"))
                        .permitAll()
                        .anyRequest().authenticated()
                );
        return http.build();
    }
}
