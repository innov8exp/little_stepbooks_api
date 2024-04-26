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
import net.stepbooks.domain.goods.entity.PhysicalGoodsEntity;
import net.stepbooks.domain.goods.service.PhysicalGoodsService;
import net.stepbooks.infrastructure.enums.PublishStatus;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Tag(name = "VirtualGoods", description = "物理产品相关接口")
@RestController
@RequestMapping("/v1/physical-goods")
@RequiredArgsConstructor
@SecurityRequirement(name = "Client Authentication")
public class PhysicalGoodsController {

    private final PhysicalGoodsService physicalGoodsService;

    @GetMapping
    @Operation(summary = "物理产品列表")
    public ResponseEntity<IPage<PhysicalGoodsEntity>> list(@RequestParam int currentPage,
                                                           @RequestParam int pageSize,
                                                           @RequestParam(required = false) String name) {
        Page<PhysicalGoodsEntity> page = Page.of(currentPage, pageSize);
        LambdaQueryWrapper<PhysicalGoodsEntity> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(PhysicalGoodsEntity::getStatus, PublishStatus.ONLINE);
        wrapper.like(ObjectUtils.isNotEmpty(name), PhysicalGoodsEntity::getName, name);
        wrapper.orderByAsc(PhysicalGoodsEntity::getSortIndex);
        IPage<PhysicalGoodsEntity> results = physicalGoodsService.page(page, wrapper);
        return ResponseEntity.ok(results);
    }
}
