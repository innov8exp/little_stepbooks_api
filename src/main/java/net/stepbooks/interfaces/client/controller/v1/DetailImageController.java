package net.stepbooks.interfaces.client.controller.v1;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import net.stepbooks.domain.media.entity.DetailImageCut;
import net.stepbooks.domain.media.service.DetailImageCutService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Tag(name = "DetailImage", description = "详情图切图相关接口")
@RestController
@RequestMapping("/v1/detail-image")
@RequiredArgsConstructor
@SecurityRequirement(name = "Client Authentication")
public class DetailImageController {

    private final DetailImageCutService detailImageCutService;

    @GetMapping("/{detailImgId}")
    @Operation(summary = "获得全部详情切图")
    public ResponseEntity<List<String>> list(@PathVariable String detailImgId) {
        LambdaQueryWrapper<DetailImageCut> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(DetailImageCut::getDetailImgId, detailImgId);
        wrapper.orderByAsc(DetailImageCut::getSortIndex);
        List<DetailImageCut> results = detailImageCutService.list(wrapper);
        List<String> imageUrls = results.stream().map(DetailImageCut::getImgUrl).collect(Collectors.toList());
        return ResponseEntity.ok(imageUrls);
    }
}
