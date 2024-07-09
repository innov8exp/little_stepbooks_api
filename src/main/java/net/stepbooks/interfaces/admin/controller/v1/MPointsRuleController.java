package net.stepbooks.interfaces.admin.controller.v1;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "PointsRule", description = "积分规则设置接口")
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/v1/points-rule")
@SecurityRequirement(name = "Admin Authentication")
public class MPointsRuleController {


}
