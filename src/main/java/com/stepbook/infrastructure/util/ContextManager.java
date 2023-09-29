package com.stepbook.infrastructure.util;

import com.stepbook.domain.user.entity.UserEntity;
import com.stepbook.domain.user.service.UserService;
import com.stepbook.infrastructure.model.JwtUserDetails;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class ContextManager {

    private final UserService userService;

    public ContextManager(UserService userService) {
        this.userService = userService;
    }

    public UserEntity currentUser() {
        JwtUserDetails details = (JwtUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return this.userService.findUserByUsername(details.getUsername());
    }

}
