package net.stepbooks.interfaces.admin.controller.v1;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import net.stepbooks.domain.user.entity.User;
import net.stepbooks.domain.user.service.UserService;
import net.stepbooks.infrastructure.assembler.BaseAssembler;
import net.stepbooks.application.dto.admin.MUserDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/v1/users")
@SecurityRequirement(name = "Admin Authentication")
public class MUserController {

    private final UserService userService;

    public MUserController(UserService userService) {
        this.userService = userService;
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable String id) {
        userService.deleteUser(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<MUserDto>> getAllUsers() {
        List<User> users = userService.findUsers();
        List<MUserDto> userDtos = BaseAssembler.convert(users, MUserDto.class);
        return ResponseEntity.ok(userDtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MUserDto> getAllUser(@PathVariable String id) {
        User user = userService.findUser(id);
        return ResponseEntity.ok(BaseAssembler.convert(user, MUserDto.class));
    }
}
