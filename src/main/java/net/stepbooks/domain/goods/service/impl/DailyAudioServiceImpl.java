package net.stepbooks.domain.goods.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import net.stepbooks.domain.goods.entity.DailyAudioEntity;
import net.stepbooks.domain.goods.entity.VirtualCategoryEntity;
import net.stepbooks.domain.goods.mapper.DailyAudioMapper;
import net.stepbooks.domain.goods.service.DailyAudioService;
import net.stepbooks.domain.goods.service.VirtualCategoryService;
import net.stepbooks.domain.goods.service.VirtualGoodsService;
import net.stepbooks.infrastructure.assembler.BaseAssembler;
import net.stepbooks.interfaces.client.dto.DailyAudioDto;
import net.stepbooks.interfaces.client.dto.VirtualGoodsDto;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
@Service
public class DailyAudioServiceImpl extends ServiceImpl<DailyAudioMapper, DailyAudioEntity>
        implements DailyAudioService {

    private final VirtualGoodsService virtualGoodsService;

    private final VirtualCategoryService virtualCategoryService;

    @Override
    public DailyAudioDto todayAudio() {
        LocalDate currentDate = LocalDate.now();
        LambdaQueryWrapper<DailyAudioEntity> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(DailyAudioEntity::getDay, currentDate);
        DailyAudioEntity dailyAudioEntity = getOne(wrapper);

        if (dailyAudioEntity != null) {
            DailyAudioDto dailyAudioDto = BaseAssembler.convert(dailyAudioEntity, DailyAudioDto.class);
            VirtualCategoryEntity virtualCategory = virtualCategoryService.getById(dailyAudioDto.getCategoryId());
            if (virtualCategory.getParentId() != null) {
                virtualCategory = virtualCategoryService.getById(virtualCategory.getParentId());
            }
            List<VirtualGoodsDto> goods = virtualGoodsService.listAll(virtualCategory.getId());
            dailyAudioDto.setGoods(goods);
            return dailyAudioDto;
        }

        DailyAudioDto dailyAudioDto = new DailyAudioDto();
        dailyAudioDto.setDay(currentDate);
        return dailyAudioDto;
    }
}
