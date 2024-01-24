package net.stepbooks.interfaces.client.controller.v1;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import net.stepbooks.domain.pairedread.entity.PairedReadCollection;
import net.stepbooks.domain.pairedread.service.PairedReadCollectionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "PairedReadCollection", description = "伴读合集相关接口")
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/paired-read-collection")
@SecurityRequirement(name = "Client Authentication")
public class PairedReadCollectionController {

    private final PairedReadCollectionService pairedReadCollectionService;

    @GetMapping("/carousel")
    @Operation(summary = "获取轮播列表")
    public ResponseEntity<List<PairedReadCollection>> getCarousels() {
        List<PairedReadCollection> data = pairedReadCollectionService.list();
        return ResponseEntity.ok(data);
    }
}
