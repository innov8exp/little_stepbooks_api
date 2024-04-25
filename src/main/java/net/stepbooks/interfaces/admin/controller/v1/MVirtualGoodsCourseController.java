package net.stepbooks.interfaces.admin.controller.v1;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import net.stepbooks.domain.goods.entity.VirtualGoodsCourseEntity;
import net.stepbooks.domain.goods.service.VirtualGoodsCourseService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "VirtualGoodsCourse", description = "虚拟产品课程后台管理接口")
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/v1/virtual-goods-course")
@SecurityRequirement(name = "Admin Authentication")
public class MVirtualGoodsCourseController {


    private final VirtualGoodsCourseService virtualGoodsCourseService;

    @PostMapping()
    @Operation(summary = "创建虚拟产品课程")
    public ResponseEntity<VirtualGoodsCourseEntity> create(@RequestBody VirtualGoodsCourseEntity entity) {
        virtualGoodsCourseService.save(entity);
        return ResponseEntity.ok(entity);
    }

    @PutMapping("/{id}")
    @Operation(summary = "修改虚拟产品课程")
    public ResponseEntity<?> update(@PathVariable String id, @RequestBody VirtualGoodsCourseEntity entity) {
        entity.setId(id);
        virtualGoodsCourseService.updateById(entity);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除虚拟产品课程")
    public ResponseEntity<?> delete(@PathVariable String id) {
        virtualGoodsCourseService.removeById(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    @Operation(summary = "虚拟产品课程详情")
    public ResponseEntity<VirtualGoodsCourseEntity> get(@PathVariable String id) {
        VirtualGoodsCourseEntity entity = virtualGoodsCourseService.getById(id);
        return ResponseEntity.ok(entity);
    }

    @GetMapping
    @Operation(summary = "虚拟产品课程查询")
    public ResponseEntity<IPage<VirtualGoodsCourseEntity>> list(@RequestParam int currentPage,
                                                                @RequestParam int pageSize) {
        Page<VirtualGoodsCourseEntity> page = Page.of(currentPage, pageSize);
        IPage<VirtualGoodsCourseEntity> results = virtualGoodsCourseService.page(page);
        return ResponseEntity.ok(results);
    }

}
