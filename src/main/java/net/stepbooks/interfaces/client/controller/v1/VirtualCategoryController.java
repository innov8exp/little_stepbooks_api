package net.stepbooks.interfaces.client.controller.v1;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.stepbooks.domain.goods.entity.VirtualCategoryEntity;
import net.stepbooks.domain.goods.enums.VirtualCategoryType;
import net.stepbooks.domain.goods.service.VirtualCategoryService;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Tag(name = "VirtualGoodsCategory", description = "虚拟产品大类相关接口")
@RestController
@RequestMapping("/v1/virtual-goods-category")
@RequiredArgsConstructor
@SecurityRequirement(name = "Client Authentication")
public class VirtualCategoryController {

    private final VirtualCategoryService virtualCategoryService;

    @GetMapping
    @Operation(summary = "虚拟产品大类列表")
    public ResponseEntity<IPage<VirtualCategoryEntity>> list(@RequestParam int currentPage,
                                                             @RequestParam int pageSize,
                                                             @RequestParam VirtualCategoryType type,
                                                             @RequestParam(required = false) String name) {
        Page<VirtualCategoryEntity> page = Page.of(currentPage, pageSize);
        LambdaQueryWrapper<VirtualCategoryEntity> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(VirtualCategoryEntity::getType, type);
        wrapper.like(ObjectUtils.isNotEmpty(name), VirtualCategoryEntity::getName, name);
        IPage<VirtualCategoryEntity> results = virtualCategoryService.page(page, wrapper);
        return ResponseEntity.ok(results);
    }
}
