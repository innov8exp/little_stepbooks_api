package net.stepbooks.interfaces.client.controller.v1;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.stepbooks.domain.goods.service.VirtualGoodsService;
import net.stepbooks.interfaces.client.dto.VirtualGoodsDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@Tag(name = "VirtualGoods", description = "虚拟产品相关接口")
@RestController
@RequestMapping("/v1/virtual-goods")
@RequiredArgsConstructor
@SecurityRequirement(name = "Client Authentication")
public class VirtualGoodsController {

    private VirtualGoodsService virtualGoodsService;

    @GetMapping("/category/{virtualCategoryId}")
    @Operation(summary = "获得虚拟产品大类下的全部小类信息")
    public ResponseEntity<List<VirtualGoodsDto>> list(@PathVariable String virtualCategoryId) {
        List<VirtualGoodsDto> virtualGoodsDtos = virtualGoodsService.listAll(virtualCategoryId);
        return ResponseEntity.ok(virtualGoodsDtos);
    }

}
