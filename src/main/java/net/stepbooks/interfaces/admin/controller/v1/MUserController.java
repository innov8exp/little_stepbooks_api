package net.stepbooks.interfaces.admin.controller.v1;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import net.stepbooks.domain.user.entity.User;
import net.stepbooks.domain.user.service.UserService;
import net.stepbooks.infrastructure.assembler.BaseAssembler;
import net.stepbooks.interfaces.admin.dto.MUserDto;
import net.stepbooks.interfaces.admin.dto.UserStatusDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/v1/users")
@SecurityRequirement(name = "Admin Authentication")
public class MUserController {

    private final UserService userService;

    @PutMapping("/{id}")
    public ResponseEntity<?> changeUserStatus(@PathVariable String id, @RequestBody UserStatusDto userStatusDto) {
        userService.changeUserStatus(id, userStatusDto.getActive());
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<IPage<User>> getAllUsers(@RequestParam int currentPage,
                                                      @RequestParam int pageSize,
                                                      @RequestParam(required = false) String username,
                                                      @RequestParam(required = false) String nickname) {
        Page<User> page = Page.of(currentPage, pageSize);
        IPage<User> users = userService.findUsers(page, username, nickname);
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MUserDto> getAllUser(@PathVariable String id) {
        User user = userService.findUser(id);
        return ResponseEntity.ok(BaseAssembler.convert(user, MUserDto.class));
    }
}
