package net.stepbooks.interfaces.admin.controller.v1;

import lombok.RequiredArgsConstructor;
import net.stepbooks.application.dto.client.InventoryDto;
import net.stepbooks.domain.inventory.entity.Inventory;
import net.stepbooks.domain.inventory.service.InventoryService;
import net.stepbooks.infrastructure.assembler.BaseAssembler;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/v1/inventories")
public class MInventoryController {

    private final InventoryService inventoryService;

    @PostMapping
    public ResponseEntity<?> createInventory(@RequestBody InventoryDto inventoryDto) {
        Inventory inventory = BaseAssembler.convert(inventoryDto, Inventory.class);
        inventoryService.createInventory(inventory);
        return ResponseEntity.ok().build();
    }
}
