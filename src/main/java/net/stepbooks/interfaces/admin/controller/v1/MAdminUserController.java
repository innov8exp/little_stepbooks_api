package net.stepbooks.interfaces.admin.controller.v1;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import net.stepbooks.domain.admin.entity.AdminUser;
import net.stepbooks.domain.admin.service.AdminUserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/v1/admin-users")
@SecurityRequirement(name = "Admin Authentication")
public class MAdminUserController {

    private final AdminUserService adminUserService;

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
