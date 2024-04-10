package net.stepbooks.interfaces.client.controller.v1;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "MemberExpiration", description = "会员有效期相关接口")
@RestController
@RequestMapping("/v1/member-expiration")
@RequiredArgsConstructor
@SecurityRequirement(name = "Client Authentication")
public class MemberExpirationController {
}
