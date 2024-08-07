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
                .anonymous(AbstractHttpConfigurer::disable)
                .sessionManagement((sessionManagement) ->
                        sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(userAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling((exceptions) -> exceptions
                        .authenticationEntryPoint(entryPointUnauthorizedHandler)
                        .accessDeniedHandler(restAccessDeniedHandler))
                .authorizeHttpRequests((requests) -> requests.requestMatchers(
                                mvc.pattern("/admin/**"),
                                mvc.pattern("/auth/**"),
                                mvc.pattern("/v1/feedbacks/zmkm"),
                                mvc.pattern("/v1/points-task/**"),
                                mvc.pattern("/v1/skus"),
                                mvc.pattern("/v1/system-config"),
                                mvc.pattern("/v1/daily-audio/**"),
                                mvc.pattern("/v1/skus/**"),
                                mvc.pattern("/v1/detail-image/**"),
                                mvc.pattern("/v1/virtual-category-product/**"),
                                mvc.pattern("/v1/virtual-goods-category/**"),
                                mvc.pattern("/v1/virtual-goods/**"),
                                mvc.pattern("/v1/payments/**"),
                                mvc.pattern("/v1/products/**"),
                                mvc.pattern("/v1/product-media"),
                                mvc.pattern("/v1/paired-read-collection/**"),
                                mvc.pattern("/v1/paired-read/**"),
                                mvc.pattern("/v1/paired-read"),
                                mvc.pattern("/v1/bookseries/**"),
                                mvc.pattern("/v1/books/{id}"),
                                mvc.pattern("/v1/books/none-series-books"),
                                mvc.pattern("/v1/books/{id}/chapters"), //临时关闭，所有人可见！！！！！
                                mvc.pattern("/v1/books/{id}/courses"), //临时关闭，所有人可见！！！！！
                                mvc.pattern("/v1/advertisements/carousel"),
                                mvc.pattern("/v1/advertisements/type"),
                                mvc.pattern("/v1/courses/*/url/trail"),
                                mvc.pattern("/actuator/**"),
                                mvc.pattern("/swagger-ui/**"),
                                mvc.pattern("/v3/api-docs/**"),
                                mvc.pattern("/swagger-ui.html"),
                                mvc.pattern("/v1/bookseries/**"),
                                mvc.pattern("/local_protocol.html"),
                                mvc.pattern("/privacy_policy.html"),
                                mvc.pattern("/user_agreement.html"),
                                mvc.pattern("/stepbooks_catalog_2024.html"),
                                mvc.pattern("/e4evlW47q4.txt")
                                )
                        .permitAll()
                        .anyRequest()
                        .authenticated()
                );
        return http.build();
    }
}
