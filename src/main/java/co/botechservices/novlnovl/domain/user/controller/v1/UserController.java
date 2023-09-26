package co.botechservices.novlnovl.domain.user.controller.v1;

import co.botechservices.novlnovl.domain.email.service.EmailService;
import co.botechservices.novlnovl.domain.order.dto.ConsumptionDto;
import co.botechservices.novlnovl.domain.order.dto.OrderDto;
import co.botechservices.novlnovl.domain.user.dto.*;
import co.botechservices.novlnovl.domain.user.entity.UserAccountEntity;
import co.botechservices.novlnovl.domain.user.entity.UserEntity;
import co.botechservices.novlnovl.domain.user.entity.UserTagRefEntity;
import co.botechservices.novlnovl.domain.user.service.UserAccountService;
import co.botechservices.novlnovl.domain.user.service.UserService;
import co.botechservices.novlnovl.infrastructure.assembler.BaseAssembler;
import co.botechservices.novlnovl.infrastructure.enums.EmailType;
import co.botechservices.novlnovl.infrastructure.exception.BusinessException;
import co.botechservices.novlnovl.infrastructure.exception.ErrorCode;
import co.botechservices.novlnovl.infrastructure.util.ContextManager;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/users")
public class UserController {

    private final UserService userService;
    private final EmailService emailService;
    private final ContextManager contextManager;
    private final UserAccountService userAccountService;

    @GetMapping("/info")
    public ResponseEntity<UserDto> getUserInfo() {
        UserEntity userEntity = contextManager.currentUser();
        UserDto userDto = BaseAssembler.convert(userEntity, UserDto.class);
        return ResponseEntity.ok(userDto);
    }

    @PutMapping("/info")
    public ResponseEntity<?> updateUserInfo(@RequestBody UserDto userDto) {
        UserEntity currentUser = contextManager.currentUser();
        UserEntity userEntity = BaseAssembler.convert(userDto, UserEntity.class);
        userService.updateUserById(currentUser.getId(), userEntity);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/avatar/upload")
    public ResponseEntity<String> uploadAvatar(@RequestParam("file") @NotNull MultipartFile file) {
        UserEntity currentUser = contextManager.currentUser();
        String url = userService.uploadImg(file, currentUser.getId());
        return ResponseEntity.ok(url);
    }

    @GetMapping("/account")
    public ResponseEntity<UserAccountDto> getUserAccount() {
        UserEntity currentUser = contextManager.currentUser();
        UserAccountEntity userAccount = userAccountService.getUserAccount(currentUser.getId());
        UserAccountDto userAccountDto = BaseAssembler.convert(userAccount, UserAccountDto.class);
        return ResponseEntity.ok(userAccountDto);
    }

    @PostMapping("/account/consume")
    public ResponseEntity<?> consumeCoin(@RequestBody ConsumptionDto consumptionDto) {
        UserEntity currentUser = contextManager.currentUser();
        consumptionDto.setUserId(currentUser.getId());
        userAccountService.consumeCoin(currentUser.getId(), consumptionDto);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/account/recharge")
    public ResponseEntity<?> rechargeCoin(@RequestBody OrderDto orderDto) {
        UserEntity currentUser = contextManager.currentUser();
        orderDto.setUserId(currentUser.getId());
        userAccountService.rechargeCoin(currentUser.getId(), orderDto);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/tags")
    public ResponseEntity<?> createTagRef(@RequestBody UserTagRefDto userTagRefDto) {
        UserEntity currentUser = contextManager.currentUser();
        userTagRefDto.setUserId(currentUser.getId());
        List<UserTagRefEntity> userTagRefEntities = userTagRefDto.getTagIds().stream().map(tagId ->
                UserTagRefEntity.builder()
                        .tagId(tagId)
                        .userId(currentUser.getId())
                        .build()).collect(Collectors.toList());
        userService.createUserTagRef(userTagRefEntities);
        return ResponseEntity.ok().build();
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
            UserEntity userEntity = contextManager.currentUser();
            userEntity.setEmail(validateDto.getEmail());
            userService.updateUserById(userEntity.getId(), userEntity);
        }
        return ResponseEntity.ok(valid);
    }

    @PostMapping("/verification")
    public ResponseEntity<?> sendVerificationCode(@Valid @RequestBody VerificationDto verificationDto) {
        if ("LINK".equals(verificationDto.getEmailType())) {
            UserEntity userEntity = contextManager.currentUser();
            if (ObjectUtils.isEmpty(userEntity)) {
                throw new BusinessException(ErrorCode.USER_NOT_FOUND);
            }
            userService.sendLinkVerificationEmail(verificationDto.getEmail());
        } else {
            throw new BusinessException(ErrorCode.BAD_REQUEST);
        }
        return ResponseEntity.ok().build();
    }
}
