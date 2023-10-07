package com.stepbook.infrastructure.security;

import com.stepbook.interfaces.admin.assembler.AdminAuthAssembler;
import com.stepbook.domain.admin.entity.AdminUserEntity;
import com.stepbook.domain.admin.service.AdminUserService;
import com.stepbook.interfaces.client.assembler.AuthAssembler;
import com.stepbook.domain.user.entity.UserEntity;
import com.stepbook.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
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
            UserEntity userEntity = userService.findUserByUsername(username);
            if (ObjectUtils.isEmpty(userEntity)) {
                throw new UsernameNotFoundException("Cannot found the user with username: " + username);
            }
            return AuthAssembler.userEntityToJwtUserDetails(userEntity);
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
