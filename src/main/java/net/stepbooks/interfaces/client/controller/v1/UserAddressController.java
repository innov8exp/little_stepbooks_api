package net.stepbooks.interfaces.client.controller.v1;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import net.stepbooks.domain.address.entity.UserAddress;
import net.stepbooks.domain.address.service.UserAddressService;
import net.stepbooks.domain.user.entity.User;
import net.stepbooks.infrastructure.util.ContextManager;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "User Address", description = "用户地址相关接口")
@RestController
@RequiredArgsConstructor
@SecurityRequirement(name = "Client Authentication")
@RequestMapping("/v1/users/addresses")
public class UserAddressController {

    private final UserAddressService userAddressService;
    private final ContextManager contextManager;

    @PostMapping
    @Operation(summary = "创建用户地址")
    public ResponseEntity<UserAddress> createUserAddress(@RequestBody UserAddress userAddress) {
        User user = contextManager.currentUser();
        userAddress.setUserId(user.getId());
        userAddressService.createUserAddress(userAddress);
        return ResponseEntity.ok(userAddress);
    }

    @GetMapping
    @Operation(summary = "获取用户地址列表")
    public ResponseEntity<List<UserAddress>> getUserAddresses() {
        User user = contextManager.currentUser();
        return ResponseEntity.ok(userAddressService.findByUserId(user.getId()));
    }

    @PutMapping("/{id}")
    @Operation(summary = "更新用户地址")
    public ResponseEntity<?> updateUserAddressById(@PathVariable String id, @RequestBody UserAddress userAddress) {
        userAddress.setId(id);
        userAddressService.updateUserAddress(userAddress);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除用户地址")
    public ResponseEntity<?> deleteById(@PathVariable String id) {
        User user = contextManager.currentUser();
        userAddressService.deleteAddress(id, user.getId());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    @Operation(summary = "获取用户地址详情")
    public ResponseEntity<UserAddress> getUserAddressById(@PathVariable String id) {
        return ResponseEntity.ok(userAddressService.getById(id));
    }

    @GetMapping("/default")
    @Operation(summary = "获取用户默认地址")
    public ResponseEntity<UserAddress> getUserDefaultAddress() {
        User user = contextManager.currentUser();
        return ResponseEntity.ok(userAddressService.findDefaultByUserId(user.getId()));
    }


}
