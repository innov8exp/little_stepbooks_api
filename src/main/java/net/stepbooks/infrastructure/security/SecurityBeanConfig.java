package net.stepbooks.infrastructure.security;

import lombok.RequiredArgsConstructor;
import net.stepbooks.domain.admin.entity.AdminUser;
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
            AdminUser adminUser = adminUserService.findUserByUsername(username);
            if (ObjectUtils.isEmpty(adminUser)) {
                throw new UsernameNotFoundException("Cannot found the user with username: " + username);
            }
            return AdminAuthAssembler.adminUserEntityToJwtUserDetails(adminUser);
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

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    MvcRequestMatcher.Builder mvc(HandlerMappingIntrospector introspector) {
        return new MvcRequestMatcher.Builder(introspector);
    }

}
