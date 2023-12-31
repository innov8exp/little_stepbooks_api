package net.stepbooks.infrastructure.util;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.stepbooks.domain.admin.entity.AdminUser;
import net.stepbooks.domain.admin.service.AdminUserService;
import net.stepbooks.domain.user.entity.User;
import net.stepbooks.domain.user.service.UserService;
import net.stepbooks.infrastructure.exception.BusinessException;
import net.stepbooks.infrastructure.exception.ErrorCode;
import net.stepbooks.infrastructure.model.JwtUserDetails;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ContextManager {

    private final UserService userService;
    private final AdminUserService adminUserService;

    public User currentUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (ObjectUtils.isEmpty(principal)) {
            throw new BusinessException(ErrorCode.AUTH_ERROR, "Cannot get the current user.");
        }
        log.debug("principal: {}", principal);
        if ("anonymousUser".equals(principal)) {
            throw new BusinessException(ErrorCode.AUTH_ERROR, "Cannot get the current user.");
        }
        JwtUserDetails details = (JwtUserDetails) principal;
        return userService.findUserByUsername(details.getUsername());
    }

    public AdminUser currentAdminUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (ObjectUtils.isEmpty(principal)) {
            throw new BusinessException(ErrorCode.AUTH_ERROR, "Cannot get the current user.");
        }
        log.debug("principal: {}", principal);
        if ("anonymousUser".equals(principal)) {
            throw new BusinessException(ErrorCode.AUTH_ERROR, "Cannot get the current user.");
        }
        JwtUserDetails details = (JwtUserDetails) principal;
        return adminUserService.findUserByUsername(details.getUsername());
    }

}
