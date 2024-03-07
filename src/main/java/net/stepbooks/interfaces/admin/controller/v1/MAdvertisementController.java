package net.stepbooks.interfaces.admin.controller.v1;

import lombok.RequiredArgsConstructor;
import net.stepbooks.domain.ads.entity.Advertisement;
import net.stepbooks.domain.ads.service.AdvertisementService;
import net.stepbooks.infrastructure.enums.AdsType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public ResponseEntity<List<Advertisement>> getAllAdvertisements() {
        List<Advertisement> advertisements = advertisementService.listAdvertisements(null);
        return ResponseEntity.ok(advertisements);
    }

    @GetMapping("/type")
    public ResponseEntity<List<Advertisement>> getTypedAdvertisements(@RequestParam AdsType adsType) {
        List<Advertisement> advertisements = advertisementService.listAdvertisements(adsType);
        return ResponseEntity.ok(advertisements);
    }
}
