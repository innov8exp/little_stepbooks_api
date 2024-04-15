package net.stepbooks.interfaces.client.controller.v1;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Tag(name = "VirtualGoods", description = "虚拟产品相关接口")
@RestController
@RequestMapping("/v1/virtual-goods")
@RequiredArgsConstructor
@SecurityRequirement(name = "Client Authentication")
public class VirtualGoodsController {
}