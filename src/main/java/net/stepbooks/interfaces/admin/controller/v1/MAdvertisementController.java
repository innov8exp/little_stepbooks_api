package net.stepbooks.interfaces.admin.controller.v1;

import lombok.RequiredArgsConstructor;
import net.stepbooks.domain.ads.entity.Advertisement;
import net.stepbooks.domain.ads.service.AdvertisementService;
import net.stepbooks.infrastructure.enums.AdsType;
import net.stepbooks.interfaces.admin.dto.AdvertisementDto;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Random;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/v1/advertisements")
public class MAdvertisementController {

    private final AdvertisementService advertisementService;

    @PostMapping
    public ResponseEntity<?> createAdvertisement(@RequestBody Advertisement advertisement) {
        advertisementService.save(advertisement);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateAdvertisement(@PathVariable String id, @RequestBody Advertisement advertisement) {
        advertisement.setId(id);
        advertisementService.updateById(advertisement);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAdvertisement(@PathVariable String id) {
        advertisementService.removeById(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Advertisement> getAdvertisement(@PathVariable String id) {
        Advertisement advertisement = advertisementService.getById(id);
        return ResponseEntity.ok(advertisement);
    }

    @GetMapping
    public ResponseEntity<List<AdvertisementDto>> getAllAdvertisements() {
        List<AdvertisementDto> advertisements = advertisementService.listAdvertisements();
        return ResponseEntity.ok(advertisements);
    }

    @GetMapping("/type")
    public ResponseEntity<List<AdvertisementDto>> getTypedAdvertisements(@RequestParam AdsType adsType) {
        List<AdvertisementDto> advertisements = advertisementService.listAdvertisementsByType(adsType);
        return ResponseEntity.ok(advertisements);
    }

    @GetMapping("/random")
    public ResponseEntity<AdvertisementDto> randomGetAds() {
        List<AdvertisementDto> advertisementDtos = advertisementService.listAdvertisementsByType(AdsType.RECOMMEND);
        if (ObjectUtils.isEmpty(advertisementDtos)) {
            return ResponseEntity.notFound().build();
        }
        Random random = new Random();
        int n = random.nextInt(advertisementDtos.size());
        AdvertisementDto advertisementDto = advertisementDtos.get(n);
        return ResponseEntity.ok(advertisementDto);
    }
}
