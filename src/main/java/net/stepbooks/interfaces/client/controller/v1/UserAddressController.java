package net.stepbooks.interfaces.client.controller.v1;

import lombok.RequiredArgsConstructor;
import net.stepbooks.domain.address.entity.UserAddress;
import net.stepbooks.domain.address.service.UserAddressService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/user-addresses")
public class UserAddressController {

    private final UserAddressService userAddressService;

    @PutMapping("/{id}")
    public ResponseEntity<?> updateById(@PathVariable String id, @RequestBody UserAddress userAddress) {
        userAddress.setId(id);
        userAddressService.updateById(userAddress);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteById(@PathVariable String id) {
        userAddressService.removeById(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserAddress> getById(@PathVariable String id) {
        return ResponseEntity.ok(userAddressService.getById(id));
    }
}
