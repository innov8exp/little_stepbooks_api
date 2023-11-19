package net.stepbooks.interfaces.admin.controller.v1;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import net.stepbooks.domain.admin.entity.AdminUser;
import net.stepbooks.domain.admin.service.AdminUserService;
import net.stepbooks.infrastructure.exception.BusinessException;
import net.stepbooks.infrastructure.exception.ErrorCode;
import net.stepbooks.infrastructure.util.ContextManager;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/v1/admin-users")
@SecurityRequirement(name = "Admin Authentication")
public class MAdminUserController {

    private final AdminUserService adminUserService;
    private final ContextManager contextManager;

    @PostMapping
    public ResponseEntity<?> createUser(@RequestBody AdminUser adminUser) {
        adminUserService.save(adminUser);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@PathVariable String id, @RequestBody AdminUser adminUser) {
        AdminUser user = contextManager.currentAdminUser();
        if (!user.getId().equals(id)) {
            throw new BusinessException(ErrorCode.ONLY_SELF_CAN_UPDATE);
        }
        adminUser.setId(id);
        adminUserService.updateById(adminUser);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable String id) {
        adminUserService.removeById(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<AdminUser>> getAllUsers() {
        List<AdminUser> users = adminUserService.list();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AdminUser> getUser(@PathVariable String id) {
        AdminUser adminUser = adminUserService.getById(id);
        return ResponseEntity.ok(adminUser);
    }
}
