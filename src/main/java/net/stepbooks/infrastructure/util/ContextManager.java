package net.stepbooks.infrastructure.util;

import net.stepbooks.domain.user.entity.User;
import net.stepbooks.domain.user.service.UserService;
import net.stepbooks.infrastructure.model.JwtUserDetails;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class ContextManager {

    private final UserService userService;

    public ContextManager(UserService userService) {
        this.userService = userService;
    }

    public User currentUser() {
        JwtUserDetails details = (JwtUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return this.userService.findUserByUsername(details.getUsername());
    }

}
