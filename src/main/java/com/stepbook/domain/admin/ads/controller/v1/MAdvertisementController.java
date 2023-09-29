package com.stepbook.domain.admin.ads.controller.v1;

import com.stepbook.domain.ads.dto.AdvertisementDto;
import com.stepbook.domain.ads.entity.AdvertisementEntity;
import com.stepbook.domain.ads.service.AdvertisementService;
import com.stepbook.infrastructure.assembler.BaseAssembler;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/v1/advertisements")
public class MAdvertisementController {

    private final AdvertisementService advertisementService;

    public MAdvertisementController(AdvertisementService advertisementService) {
        this.advertisementService = advertisementService;
    }

    @PostMapping
    public ResponseEntity<?> createAdvertisement(@RequestBody AdvertisementDto advertisementDto) {
        AdvertisementEntity entity = BaseAssembler.convert(advertisementDto, AdvertisementEntity.class);
        advertisementService.createAdvertisement(entity);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateAdvertisement(@PathVariable String id, @RequestBody AdvertisementDto advertisementDto) {
        AdvertisementEntity entity = BaseAssembler.convert(advertisementDto, AdvertisementEntity.class);
        advertisementService.updateAdvertisement(id, entity);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAdvertisement(@PathVariable String id) {
        advertisementService.deleteAdvertisement(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<AdvertisementDto> getAdvertisement(@PathVariable String id) {
        AdvertisementDto advertisement = advertisementService.findAdsById(id);
        return ResponseEntity.ok(advertisement);
    }

    @GetMapping
    public ResponseEntity<List<AdvertisementDto>> getAllAdvertisements() {
        List<AdvertisementDto> advertisements = advertisementService.listAllAdvertisements(null);
        return ResponseEntity.ok(advertisements);
    }


}
