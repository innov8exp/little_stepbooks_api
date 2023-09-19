package co.botechservices.novlnovl.domain.ads.controller.v1;

import co.botechservices.novlnovl.domain.ads.dto.AdvertisementDto;
import co.botechservices.novlnovl.domain.ads.service.AdvertisementService;
import co.botechservices.novlnovl.infrastructure.enums.AdsType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Random;

@RestController
@RequestMapping("/v1/advertisements")
public class AdvertisementController {

    private final AdvertisementService advertisementService;

    public AdvertisementController(AdvertisementService advertisementService) {
        this.advertisementService = advertisementService;
    }

    @GetMapping("/type")
    public ResponseEntity<List<AdvertisementDto>> getAllAdvertisements(@RequestParam AdsType adsType) {
        List<AdvertisementDto> advertisements = advertisementService.listAllAdvertisements(adsType);
        return ResponseEntity.ok(advertisements);
    }

    @GetMapping("/random")
    public ResponseEntity<AdvertisementDto> randomGetAds() {
        List<AdvertisementDto> advertisementDtos = advertisementService.listAllAdvertisements(AdsType.RECOMMEND);
        if (ObjectUtils.isEmpty(advertisementDtos)) {
            return ResponseEntity.notFound().build();
        }
        Random random = new Random();
        int n = random.nextInt(advertisementDtos.size());
        AdvertisementDto advertisementDto = advertisementDtos.get(n);
        return ResponseEntity.ok(advertisementDto);
    }
}
