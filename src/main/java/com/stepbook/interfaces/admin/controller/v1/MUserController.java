package com.stepbook.interfaces.admin.controller.v1;

import com.stepbook.interfaces.admin.dto.MUserDto;
import com.stepbook.domain.user.entity.UserEntity;
import com.stepbook.domain.user.service.UserService;
import com.stepbook.infrastructure.assembler.BaseAssembler;
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
