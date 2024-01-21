package net.stepbooks.interfaces.client.controller.v1;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import net.stepbooks.domain.pairedread.entity.PairedReadCollectionUser;
import net.stepbooks.domain.pairedread.service.PairedReadCollectionUserService;
import net.stepbooks.infrastructure.assembler.BaseAssembler;
import net.stepbooks.interfaces.admin.dto.PairedReadCollectionDto;
import net.stepbooks.interfaces.client.dto.PairedReadCollectionInfoDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "PairedReadCollection", description = "伴读合集相关接口")
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/paired-read-collection-user")
@SecurityRequirement(name = "Client Authentication")
public class PairedReadCollectionUserController {

    private final PairedReadCollectionUserService pairedReadCollectionUserService;

    @PostMapping
    @Operation(summary = "领取合集")
    public ResponseEntity<?> create(@RequestBody PairedReadCollectionDto dto) {
        PairedReadCollectionUser entity = BaseAssembler.convert(dto, PairedReadCollectionUser.class);
        pairedReadCollectionUserService.create(entity);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/all/{userId}")
    @Operation(summary = "查看已领取合集")
    public ResponseEntity<List<PairedReadCollectionInfoDto>> getAll(@PathVariable String userId) {
        List<PairedReadCollectionInfoDto> readCollectionUserDtoList = pairedReadCollectionUserService.getAllByUserId(userId);
        return ResponseEntity.ok(readCollectionUserDtoList);
    }
}
