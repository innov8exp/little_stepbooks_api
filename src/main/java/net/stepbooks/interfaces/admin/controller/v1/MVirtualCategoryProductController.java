package net.stepbooks.interfaces.admin.controller.v1;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import net.stepbooks.domain.goods.entity.VirtualCategoryProductEntity;
import net.stepbooks.domain.goods.service.VirtualCategoryProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "VirtualCategoryProduct", description = "虚拟大类产品对应关系后台管理接口")
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/v1/virtual-category-product")
@SecurityRequirement(name = "Admin Authentication")
public class MVirtualCategoryProductController {

    private final VirtualCategoryProductService virtualCategoryProductService;

    @PutMapping("/set")
    @Operation(summary = "设置虚拟大类产品对应关系")
    public ResponseEntity<?> set(@RequestBody VirtualCategoryProductEntity entity) {
        String categoryId = entity.getCategoryId();
        String productId = entity.getProductId();
        virtualCategoryProductService.set(categoryId, productId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/category/{categoryId}")
    @Operation(summary = "根据虚拟大类ID删除其对应关系")
    public ResponseEntity<?> delete(@PathVariable String categoryId) {
        LambdaQueryWrapper<VirtualCategoryProductEntity> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(VirtualCategoryProductEntity::getCategoryId, categoryId);
        virtualCategoryProductService.remove(wrapper);
        return ResponseEntity.ok().build();
    }

}
