package net.stepbooks.interfaces.admin.controller.v1;

import net.stepbooks.domain.coin.entity.CoinEntity;
import net.stepbooks.domain.coin.service.CoinService;
import net.stepbooks.infrastructure.assembler.BaseAssembler;
import net.stepbooks.interfaces.client.dto.CoinDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/v1/products")
public class MCoinController {

    private final CoinService rechargeCoinService;

    public MCoinController(CoinService rechargeCoinService) {
        this.rechargeCoinService = rechargeCoinService;
    }

    @PostMapping
    public ResponseEntity<?> createRechargeProduct(@RequestBody CoinDto rechargeCoinDto) {
        CoinEntity entity = BaseAssembler.convert(rechargeCoinDto, CoinEntity.class);
        rechargeCoinService.createCoinProduct(entity);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateRechargeProduct(@PathVariable String id, @RequestBody CoinDto rechargeCoinDto) {
        CoinEntity entity = BaseAssembler.convert(rechargeCoinDto, CoinEntity.class);
        rechargeCoinService.updateCoinProduct(id, entity);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteRechargeProduct(@PathVariable String id) {
        rechargeCoinService.deleteCoinProduct(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<CoinDto>> getAllProducts() {
        List<CoinEntity> categories = rechargeCoinService.findCoinProducts();
        List<CoinDto> rechargeCoinDtos = BaseAssembler.convert(categories, CoinDto.class);
        return ResponseEntity.ok(rechargeCoinDtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CoinDto> getRechargeProduct(@PathVariable String id) {
        CoinEntity rechargeProduct = rechargeCoinService.findCoinProduct(id);
        return ResponseEntity.ok(BaseAssembler.convert(rechargeProduct, CoinDto.class));
    }
}
