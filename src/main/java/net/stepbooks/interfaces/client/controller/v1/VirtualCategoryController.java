package net.stepbooks.interfaces.client.controller.v1;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.stepbooks.domain.goods.entity.VirtualCategoryEntity;
import net.stepbooks.domain.goods.entity.VirtualGoodsExpirationEntity;
import net.stepbooks.domain.goods.service.VirtualCategoryService;
import net.stepbooks.domain.goods.service.VirtualGoodsExpirationService;
import net.stepbooks.domain.user.entity.User;
import net.stepbooks.infrastructure.util.ContextManager;
import net.stepbooks.interfaces.client.dto.VirtualCategoryDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Slf4j
@Tag(name = "VirtualGoodsCategory", description = "虚拟产品大类相关接口")
@RestController
@RequestMapping("/v1/virtual-goods-category")
@RequiredArgsConstructor
@SecurityRequirement(name = "Client Authentication")
public class VirtualCategoryController {

    private final ContextManager contextManager;
    private final VirtualCategoryService virtualCategoryService;
    private final VirtualGoodsExpirationService virtualGoodsExpirationService;

    @GetMapping("/{categoryId}")
    @Operation(summary = "虚拟产品大类详情")
    public ResponseEntity<VirtualCategoryDto> get(@PathVariable String categoryId) {
        VirtualCategoryDto dto = virtualCategoryService.getFullVirtualCategoryById(categoryId);
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/my")
    @Operation(summary = "获得当前用户购买的虚拟产品大类（不包含子类）")
    public ResponseEntity<List<VirtualCategoryDto>> my() {
        User user = contextManager.currentUser();
        List<VirtualGoodsExpirationEntity> results = virtualGoodsExpirationService.validExpirations(user.getId());
        List<VirtualCategoryDto> virtualCategories = new ArrayList<>();
        HashSet<String> doneSet = new HashSet<>();
        for (VirtualGoodsExpirationEntity virtualGoodsExpirationEntity : results) {
            String categoryId = virtualGoodsExpirationEntity.getCategoryId();
            VirtualCategoryEntity virtualCategoryEntity = virtualCategoryService.getById(categoryId);
            String parentId = virtualCategoryEntity.getParentId();
            if (parentId != null) {
                //有父类的话，直接加父类
                if (!doneSet.contains(parentId)) {
                    VirtualCategoryDto dto = virtualCategoryService.getVirtualCategoryById(parentId);
                    virtualCategories.add(dto);
                    doneSet.add(parentId);
                }
                continue;
            }

            VirtualCategoryDto dto = virtualCategoryService.getVirtualCategoryById(categoryId);
            virtualCategories.add(dto);
        }
        return ResponseEntity.ok(virtualCategories);
    }

    @GetMapping("/all")
    @Operation(summary = "获得全部音视频虚拟产品大类")
    public ResponseEntity<List<VirtualCategoryDto>> getAllMediaVirtualCategories(@RequestParam(required = false) String tag) {
        List<VirtualCategoryDto> dtos = virtualCategoryService.getAllMediaVirtualCategories(tag);
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/free")
    @Operation(summary = "获得免费的音视频虚拟产品大类")
    public ResponseEntity<List<VirtualCategoryDto>> getFreeMediaVirtualCategories(@RequestParam(required = false) String tag) {
        List<VirtualCategoryDto> dtos = virtualCategoryService.getFreeMediaVirtualCategories(tag);
        return ResponseEntity.ok(dtos);
    }
}
