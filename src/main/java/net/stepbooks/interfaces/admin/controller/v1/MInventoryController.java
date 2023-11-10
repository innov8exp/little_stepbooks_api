package net.stepbooks.interfaces.admin.controller.v1;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import net.stepbooks.domain.inventory.entity.Inventory;
import net.stepbooks.domain.inventory.service.InventoryService;
import net.stepbooks.infrastructure.assembler.BaseAssembler;
import net.stepbooks.interfaces.admin.dto.InventoryQueryDto;
import net.stepbooks.interfaces.admin.dto.MInventoryDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/v1/inventories")
public class MInventoryController {

    private final InventoryService inventoryService;

    @PostMapping
    public ResponseEntity<?> createInventory(@RequestBody MInventoryDto inventoryDto) {
        Inventory inventory = BaseAssembler.convert(inventoryDto, Inventory.class);
        inventoryService.createInventory(inventory);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<IPage<MInventoryDto>> getPagedInventories(@RequestParam int currentPage,
                                                                    @RequestParam int pageSize,
                                                                    @RequestParam(required = false) String skuCode) {
        InventoryQueryDto queryDto = InventoryQueryDto.builder().skuCode(skuCode).build();
        Page<MInventoryDto> page = Page.of(currentPage, pageSize);
        IPage<MInventoryDto> inventories = inventoryService.findInventoriesInPagingByCriteria(page, queryDto);
        return ResponseEntity.ok(inventories);
    }
}
