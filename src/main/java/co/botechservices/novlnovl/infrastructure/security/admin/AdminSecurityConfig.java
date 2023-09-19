package co.botechservices.novlnovl.infrastructure.security.admin;

import co.botechservices.novlnovl.infrastructure.security.EntryPointUnauthorizedHandler;
import co.botechservices.novlnovl.infrastructure.security.RestAccessDeniedHandler;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

@Configuration
@Order(2)
public class AdminSecurityConfig extends WebSecurityConfigurerAdapter {
    private final UserDetailsService adminDetailsService;
    private final AdminAuthenticationTokenFilter adminAuthenticationTokenFilter;
    private final EntryPointUnauthorizedHandler entryPointUnauthorizedHandler;
    private final RestAccessDeniedHandler restAccessDeniedHandler;

    public AdminSecurityConfig(
            @Qualifier("adminDetailsServiceImpl") UserDetailsService adminDetailsService,
            AdminAuthenticationTokenFilter adminAuthenticationTokenFilter,
            EntryPointUnauthorizedHandler entryPointUnauthorizedHandler,
            RestAccessDeniedHandler restAccessDeniedHandler) {
        this.adminDetailsService = adminDetailsService;
        this.adminAuthenticationTokenFilter = adminAuthenticationTokenFilter;
        this.entryPointUnauthorizedHandler = entryPointUnauthorizedHandler;
        this.restAccessDeniedHandler = restAccessDeniedHandler;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(adminDetailsService);
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                .ignoringAntMatchers("/admin/auth/login", "/admin/auth/register")
                .and()
                .authorizeRequests()
                .antMatchers(
                        "/admin/auth/login",
                        "/admin/auth/refresh-token",
                        "/admin/auth/logout",
                        "/admin/auth/register",
                        "/v3/api-docs/**",
                        "/swagger-ui/**",
                        "/swagger-ui.html",
                        "/actuator/**")
                .permitAll()
                .anyRequest().authenticated().and().sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .addFilterBefore(adminAuthenticationTokenFilter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling()
                .authenticationEntryPoint(entryPointUnauthorizedHandler)
                .accessDeniedHandler(restAccessDeniedHandler);

    }
}
