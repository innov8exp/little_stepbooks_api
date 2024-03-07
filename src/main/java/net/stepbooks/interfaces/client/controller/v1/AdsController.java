package net.stepbooks.interfaces.client.controller.v1;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import net.stepbooks.domain.ads.entity.Advertisement;
import net.stepbooks.domain.ads.service.AdvertisementService;
import net.stepbooks.infrastructure.enums.AdsType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "Advertisement", description = "广告相关接口")
@RestController
@RequestMapping("/v1/advertisements")
@RequiredArgsConstructor
public class AdsController {

    private final AdvertisementService advertisementService;

    @Deprecated
    @GetMapping("/carousel")
    @Operation(summary = "获取轮播图列表")
    public ResponseEntity<List<Advertisement>> getCarouselAds() {
        List<Advertisement> advertisements = advertisementService.listAdvertisements(AdsType.CAROUSEL);
        return ResponseEntity.ok(advertisements);
    }


    @GetMapping("/type")
    @Operation(summary = "根据广告位获得广告信息")
    public ResponseEntity<List<Advertisement>> getTypedAdvertisements(@RequestParam AdsType adsType) {
        List<Advertisement> advertisements = advertisementService.listAdvertisements(adsType);
        return ResponseEntity.ok(advertisements);
    }
}
