package net.stepbooks.interfaces.client.controller.v1;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import net.stepbooks.domain.address.entity.UserAddress;
import net.stepbooks.domain.address.service.UserAddressService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "User Address", description = "用户地址相关接口")
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/user-addresses")
public class UserAddressController {

    private final UserAddressService userAddressService;

    @PutMapping("/{id}")
    @Operation(summary = "更新用户地址")
    public ResponseEntity<?> updateById(@PathVariable String id, @RequestBody UserAddress userAddress) {
        userAddress.setId(id);
        userAddressService.updateById(userAddress);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除用户地址")
    public ResponseEntity<?> deleteById(@PathVariable String id) {
        userAddressService.removeById(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    @Operation(summary = "获取用户地址详情")
    public ResponseEntity<UserAddress> getById(@PathVariable String id) {
        return ResponseEntity.ok(userAddressService.getById(id));
    }
}
