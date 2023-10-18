package net.stepbooks.interfaces.admin.controller.v1;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import net.stepbooks.domain.order.entity.ConsumptionEntity;
import net.stepbooks.domain.order.service.ConsumptionService;
import net.stepbooks.infrastructure.assembler.BaseAssembler;
import net.stepbooks.interfaces.admin.dto.ConsumptionInfoDto;
import net.stepbooks.interfaces.client.dto.ConsumptionDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/v1/consumptions")
public class MConsumptionController {

    private final ConsumptionService consumeDetailService;

    public MConsumptionController(ConsumptionService consumeDetailService) {
        this.consumeDetailService = consumeDetailService;
    }

    @PostMapping
    public ResponseEntity<?> createConsumption(@RequestBody ConsumptionDto consumptionDto) {
        ConsumptionEntity entity = BaseAssembler.convert(consumptionDto, ConsumptionEntity.class);
        consumeDetailService.createConsumption(entity);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateConsumption(@PathVariable String id, @RequestBody ConsumptionDto consumptionDto) {
        ConsumptionEntity entity = BaseAssembler.convert(consumptionDto, ConsumptionEntity.class);
        consumeDetailService.updateConsumption(id, entity);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteConsumption(@PathVariable String id) {
        consumeDetailService.deleteConsumption(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/search")
    public ResponseEntity<IPage<ConsumptionInfoDto>> getAllConsumptions(@RequestParam int currentPage,
                                                                        @RequestParam int pageSize,
                                                                        @RequestParam(required = false) String bookName,
                                                                        @RequestParam(required = false) String username,
                                                                        @RequestParam(required = false) String consumeOrderNo) {
        Page<ConsumptionInfoDto> page = Page.of(currentPage, pageSize);
        IPage<ConsumptionInfoDto> consumptionInfoDtos =
                consumeDetailService.findByCriteria(page, bookName, username, consumeOrderNo);
        return ResponseEntity.ok(consumptionInfoDtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ConsumptionDto> getAllConsumption(@PathVariable String id) {
        ConsumptionEntity consumptionEntity = consumeDetailService.findConsumption(id);
        return ResponseEntity.ok(BaseAssembler.convert(consumptionEntity, ConsumptionDto.class));
    }


}
