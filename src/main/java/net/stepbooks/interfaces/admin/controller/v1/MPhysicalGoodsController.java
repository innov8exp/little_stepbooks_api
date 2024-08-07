package net.stepbooks.interfaces.admin.controller.v1;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import net.stepbooks.domain.goods.entity.PhysicalGoodsEntity;
import net.stepbooks.domain.goods.service.PhysicalGoodsService;
import net.stepbooks.infrastructure.enums.PublishStatus;
import net.stepbooks.infrastructure.enums.StoreType;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "PhysicalGoods", description = "物理产品后台管理接口")
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/v1/physical-goods")
@SecurityRequirement(name = "Admin Authentication")
public class MPhysicalGoodsController {

    private final PhysicalGoodsService physicalGoodsService;

    @PostMapping()
    @Operation(summary = "创建物理产品")
    public ResponseEntity<PhysicalGoodsEntity> create(@RequestBody PhysicalGoodsEntity entity) {
        entity.setStatus(PublishStatus.OFFLINE);
        physicalGoodsService.save(entity);
        return ResponseEntity.ok(entity);
    }

    @PutMapping("/{id}")
    @Operation(summary = "修改物理产品")
    public ResponseEntity<?> update(@PathVariable String id, @RequestBody PhysicalGoodsEntity entity) {
        //entity.setStatus(PublishStatus.OFFLINE);
        entity.setId(id);
        physicalGoodsService.updateById(entity);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/online")
    @Operation(summary = "上线物理产品")
    public ResponseEntity<?> online(@PathVariable String id) {
        PhysicalGoodsEntity entity = physicalGoodsService.getById(id);
        entity.setStatus(PublishStatus.ONLINE);
        entity.setId(id);
        physicalGoodsService.updateById(entity);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/offline")
    @Operation(summary = "下线物理产品")
    public ResponseEntity<?> offline(@PathVariable String id) {
        PhysicalGoodsEntity entity = physicalGoodsService.getById(id);
        entity.setStatus(PublishStatus.OFFLINE);
        entity.setId(id);
        physicalGoodsService.updateById(entity);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除物理产品")
    public ResponseEntity<?> delete(@PathVariable String id) {
        physicalGoodsService.removeById(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    @Operation(summary = "物理产品详情")
    public ResponseEntity<PhysicalGoodsEntity> get(@PathVariable String id) {
        PhysicalGoodsEntity entity = physicalGoodsService.getById(id);
        return ResponseEntity.ok(entity);
    }

    @GetMapping
    @Operation(summary = "物理产品查询，如果不传storeType，默认为REGULAR，如需查积分订单，请设置为POINTS")
    public ResponseEntity<IPage<PhysicalGoodsEntity>> list(@RequestParam int currentPage,
                                                           @RequestParam int pageSize,
                                                           @RequestParam(required = false) String name,
                                                           @RequestParam(required = false) StoreType storeType) {
        if (storeType == null) {
            storeType = StoreType.REGULAR;
        }
        Page<PhysicalGoodsEntity> page = Page.of(currentPage, pageSize);
        LambdaQueryWrapper<PhysicalGoodsEntity> wrapper = Wrappers.lambdaQuery();
        wrapper.like(ObjectUtils.isNotEmpty(name), PhysicalGoodsEntity::getName, name);
        wrapper.eq(PhysicalGoodsEntity::getStoreType, storeType);
        wrapper.orderByDesc(PhysicalGoodsEntity::getSortIndex);
        IPage<PhysicalGoodsEntity> results = physicalGoodsService.page(page, wrapper);
        return ResponseEntity.ok(results);
    }

}
