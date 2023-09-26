package co.botechservices.novlnovl.infrastructure.security.user;

import co.botechservices.novlnovl.domain.user.assembler.AuthAssembler;
import co.botechservices.novlnovl.domain.user.entity.UserEntity;
import co.botechservices.novlnovl.domain.user.service.UserService;
import co.botechservices.novlnovl.infrastructure.security.EntryPointUnauthorizedHandler;
import co.botechservices.novlnovl.infrastructure.security.RestAccessDeniedHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.util.ObjectUtils;

@Order(1)
@Configuration
@RequiredArgsConstructor
public class UserSecurityConfig {

    private final UserService userService;
    private final UserAuthenticationTokenFilter userAuthenticationTokenFilter;
    private final EntryPointUnauthorizedHandler entryPointUnauthorizedHandler;
    private final RestAccessDeniedHandler restAccessDeniedHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(httpSecurityCsrfConfigurer -> httpSecurityCsrfConfigurer.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()))
                .authorizeHttpRequests((authz) -> authz.requestMatchers("/auth/login",
                                "/auth/social-login",
                                "/auth/guest-login",
                                "/auth/refresh-token",
                                "/auth/logout",
                                "/auth/register",
                                "/auth/reset-password",
                                "/auth/validation",
                                "/auth/verification",
                                "/admin/**",
                                "/v3/api-docs/**",
                                "/swagger-ui/**",
                                "/swagger-ui.html",
                                "/actuator/**")
                        .permitAll()
                        .anyRequest().authenticated()
                )
                .sessionManagement((sessionManagement) -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(userAuthenticationTokenFilter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling((exceptionHandling) -> exceptionHandling.authenticationEntryPoint(entryPointUnauthorizedHandler)
                        .accessDeniedHandler(restAccessDeniedHandler));
        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return username -> {
            UserEntity userEntity = userService.findUserByUsername(username);
            if (ObjectUtils.isEmpty(userEntity)) {
                throw new UsernameNotFoundException("Cannot found the user with username: " + username);
            }
            return AuthAssembler.userEntityToJwtUserDetails(userEntity);
        };
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
