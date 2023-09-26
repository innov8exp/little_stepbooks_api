package co.botechservices.novlnovl.infrastructure.util;

import co.botechservices.novlnovl.domain.user.entity.UserEntity;
import co.botechservices.novlnovl.domain.user.service.UserService;
import co.botechservices.novlnovl.infrastructure.model.JwtUserDetails;
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
