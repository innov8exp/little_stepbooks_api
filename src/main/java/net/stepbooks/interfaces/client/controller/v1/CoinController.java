package net.stepbooks.interfaces.client.controller.v1;

import net.stepbooks.interfaces.client.dto.CoinDto;
import net.stepbooks.domain.coin.entity.CoinEntity;
import net.stepbooks.domain.coin.service.CoinService;
import net.stepbooks.infrastructure.assembler.BaseAssembler;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/v1/coins")
public class CoinController {

    private final CoinService coinService;

    public CoinController(CoinService coinService) {
        this.coinService = coinService;
    }

    @GetMapping
    public ResponseEntity<List<CoinDto>> getAllProducts(@RequestParam String platform) {
        List<CoinEntity> entities = coinService.findCoinProductsByPlatform(platform);
        List<CoinDto> coinDtos = BaseAssembler.convert(entities, CoinDto.class);
        return ResponseEntity.ok(coinDtos);
    }

}
