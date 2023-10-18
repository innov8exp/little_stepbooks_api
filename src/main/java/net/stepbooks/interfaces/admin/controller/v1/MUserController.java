package net.stepbooks.interfaces.admin.controller.v1;

import net.stepbooks.domain.user.entity.UserEntity;
import net.stepbooks.domain.user.service.UserService;
import net.stepbooks.infrastructure.assembler.BaseAssembler;
import net.stepbooks.interfaces.admin.dto.MUserDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/v1/users")
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
        List<UserEntity> users = userService.findUsers();
        List<MUserDto> userDtos = BaseAssembler.convert(users, MUserDto.class);
        return ResponseEntity.ok(userDtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MUserDto> getAllUser(@PathVariable String id) {
        UserEntity userEntity = userService.findUser(id);
        return ResponseEntity.ok(BaseAssembler.convert(userEntity, MUserDto.class));
    }
}
