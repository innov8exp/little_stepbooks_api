package net.stepbooks.interfaces.client.controller.v1;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import net.stepbooks.interfaces.client.dto.UserDto;
import net.stepbooks.interfaces.client.dto.ValidateDto;
import net.stepbooks.interfaces.client.dto.VerificationDto;
import net.stepbooks.domain.email.service.EmailService;
import net.stepbooks.domain.media.entity.Media;
import net.stepbooks.domain.user.entity.User;
import net.stepbooks.domain.user.service.UserService;
import net.stepbooks.infrastructure.assembler.BaseAssembler;
import net.stepbooks.infrastructure.enums.EmailType;
import net.stepbooks.infrastructure.exception.BusinessException;
import net.stepbooks.infrastructure.exception.ErrorCode;
import net.stepbooks.infrastructure.util.ContextManager;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/users")
@SecurityRequirement(name = "Client Authentication")
public class UserController {

    private final UserService userService;
    private final EmailService emailService;
    private final ContextManager contextManager;

    @GetMapping("/info")
    public ResponseEntity<UserDto> getUserInfo() {
        User user = contextManager.currentUser();
        UserDto userDto = BaseAssembler.convert(user, UserDto.class);
        return ResponseEntity.ok(userDto);
    }

    @PutMapping("/info")
    public ResponseEntity<?> updateUserInfo(@RequestBody UserDto userDto) {
        User currentUser = contextManager.currentUser();
        User user = BaseAssembler.convert(userDto, User.class);
        userService.updateUserById(currentUser.getId(), user);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/avatar/upload")
    public ResponseEntity<Media> uploadAvatar(@RequestParam("file") @NotNull MultipartFile file) {
        User currentUser = contextManager.currentUser();
        Media media = userService.uploadImg(file, currentUser.getId());
        return ResponseEntity.ok(media);
    }

    @PostMapping("/link-email")
    public ResponseEntity<Boolean> linkEmail(@Valid @RequestBody ValidateDto validateDto) {
        Boolean valid = emailService.verifyValidationCode(validateDto.getEmail(),
                validateDto.getCode(), EmailType.valueOf(validateDto.getEmailType()));
        if (valid) {
            Boolean existsUserByEmail = userService.existsUserByEmail(validateDto.getEmail());
            if (existsUserByEmail) {
                throw new BusinessException(ErrorCode.EMAIL_EXISTS_ERROR);
            }
            User user = contextManager.currentUser();
            user.setEmail(validateDto.getEmail());
            userService.updateUserById(user.getId(), user);
        }
        return ResponseEntity.ok(valid);
    }

    @PostMapping("/verification")
    public ResponseEntity<?> sendVerificationCode(@Valid @RequestBody VerificationDto verificationDto) {
        if ("LINK".equals(verificationDto.getEmailType())) {
            User user = contextManager.currentUser();
            if (ObjectUtils.isEmpty(user)) {
                throw new BusinessException(ErrorCode.USER_NOT_FOUND);
            }
            userService.sendLinkVerificationEmail(verificationDto.getEmail());
        } else {
            throw new BusinessException(ErrorCode.BAD_REQUEST);
        }
        return ResponseEntity.ok().build();
    }
}
