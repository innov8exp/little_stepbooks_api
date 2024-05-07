package net.stepbooks.interfaces.client.controller.v1;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.stepbooks.domain.goods.entity.VirtualGoodsExpirationEntity;
import net.stepbooks.domain.goods.service.VirtualCategoryService;
import net.stepbooks.domain.goods.service.VirtualGoodsExpirationService;
import net.stepbooks.domain.user.entity.User;
import net.stepbooks.infrastructure.util.ContextManager;
import net.stepbooks.interfaces.client.dto.VirtualCategoryDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
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

    @GetMapping("/my")
    @Operation(summary = "获得当前用户购买的虚拟产品大类")
    public ResponseEntity<List<VirtualCategoryDto>> my() {
        User user = contextManager.currentUser();
        List<VirtualGoodsExpirationEntity> results = virtualGoodsExpirationService.validExpirations(user.getId());
        List<VirtualCategoryDto> virtualCategories = new ArrayList<>();
        for (VirtualGoodsExpirationEntity virtualGoodsExpirationEntity : results) {
            String categoryId = virtualGoodsExpirationEntity.getCategoryId();
            VirtualCategoryDto dto = virtualCategoryService.getFullVirtualCategoryById(categoryId);
            virtualCategories.add(dto);
        }
        return ResponseEntity.ok(virtualCategories);
    }

    @GetMapping("/all")
    @Operation(summary = "获得全部音视频虚拟产品大类")
    public ResponseEntity<List<VirtualCategoryDto>> getAllMediaVirtualCategories() {
        List<VirtualCategoryDto> dtos = virtualCategoryService.getAllMediaVirtualCategories();
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/free")
    @Operation(summary = "获得免费的音视频虚拟产品大类")
    public ResponseEntity<List<VirtualCategoryDto>> getFreeMediaVirtualCategories() {
        List<VirtualCategoryDto> dtos = virtualCategoryService.getFreeMediaVirtualCategories();
        return ResponseEntity.ok(dtos);
    }
}
