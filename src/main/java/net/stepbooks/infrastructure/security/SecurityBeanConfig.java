package net.stepbooks.infrastructure.security;

import lombok.RequiredArgsConstructor;
import net.stepbooks.domain.admin.entity.AdminUserEntity;
import net.stepbooks.domain.admin.service.AdminUserService;
import net.stepbooks.domain.user.entity.User;
import net.stepbooks.domain.user.service.UserService;
import net.stepbooks.interfaces.admin.assembler.AdminAuthAssembler;
import net.stepbooks.interfaces.client.assembler.AuthAssembler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.util.ObjectUtils;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

@Configuration
@RequiredArgsConstructor
public class SecurityBeanConfig {

    private final AdminUserService adminUserService;
    private final UserService userService;

    @Bean
    public UserDetailsService adminUserDetailsService() {
        return username -> {
            AdminUserEntity adminUserEntity = adminUserService.findUserByUsername(username);
            if (ObjectUtils.isEmpty(adminUserEntity)) {
                throw new UsernameNotFoundException("Cannot found the user with username: " + username);
            }
            return AdminAuthAssembler.adminUserEntityToJwtUserDetails(adminUserEntity);
        };
    }

    @Bean
    public UserDetailsService endUserDetailsService() {
        return username -> {
            User user = userService.findUserByUsername(username);
            if (ObjectUtils.isEmpty(user)) {
                throw new UsernameNotFoundException("Cannot found the user with username: " + username);
            }
            return AuthAssembler.userEntityToJwtUserDetails(user);
        };
    }

//    @Bean
//    public AuthenticationProvider adminAuthenticationProvider() {
//        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
//        authProvider.setUserDetailsService(adminUserDetailsService());
//        authProvider.setPasswordEncoder(adminPasswordEncoder());
//        return authProvider;
//    }
//
//    @Bean
//    public AuthenticationManager adminAuthenticationManager(AuthenticationConfiguration config) throws Exception {
//        return config.getAuthenticationManager();
//    }

    //    @Bean
//    public AuthenticationProvider userAuthenticationProvider() {
//        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
//        authProvider.setUserDetailsService(endUserDetailsService());
//        authProvider.setPasswordEncoder(passwordEncoder());
//        return authProvider;
//    }
//
//    @Bean
//    public AuthenticationManager userAuthenticationManager(AuthenticationConfiguration config) throws Exception {
//        return config.getAuthenticationManager();
//    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    MvcRequestMatcher.Builder mvc(HandlerMappingIntrospector introspector) {
        return new MvcRequestMatcher.Builder(introspector);
    }

}
