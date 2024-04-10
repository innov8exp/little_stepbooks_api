package net.stepbooks.interfaces.admin.controller.v1;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/v1/physical-goods")
@SecurityRequirement(name = "Admin Authentication")
public class MPhysicalGoodsController {
}
