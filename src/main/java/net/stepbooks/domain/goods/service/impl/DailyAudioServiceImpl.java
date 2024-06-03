package net.stepbooks.domain.goods.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import net.stepbooks.domain.goods.entity.DailyAudioEntity;
import net.stepbooks.domain.goods.entity.VirtualCategoryEntity;
import net.stepbooks.domain.goods.entity.VirtualGoodsAudioEntity;
import net.stepbooks.domain.goods.mapper.DailyAudioMapper;
import net.stepbooks.domain.goods.service.DailyAudioService;
import net.stepbooks.domain.goods.service.VirtualCategoryService;
import net.stepbooks.domain.goods.service.VirtualGoodsService;
import net.stepbooks.infrastructure.assembler.BaseAssembler;
import net.stepbooks.interfaces.client.dto.DailyAudioDto;
import net.stepbooks.interfaces.client.dto.VirtualGoodsDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
@Service
public class DailyAudioServiceImpl extends ServiceImpl<DailyAudioMapper, DailyAudioEntity>
        implements DailyAudioService {

    private final VirtualGoodsService virtualGoodsService;

    private final VirtualCategoryService virtualCategoryService;

    private List<VirtualGoodsDto> listGoods(String categoryId) {
        VirtualCategoryEntity virtualCategory = virtualCategoryService.getById(categoryId);
        if (virtualCategory.getParentId() != null) {
            virtualCategory = virtualCategoryService.getById(virtualCategory.getParentId());
        }
        List<VirtualGoodsDto> goods = virtualGoodsService.listAll(virtualCategory.getId());
        return goods;
    }

    @Transactional
    @Override
    public DailyAudioDto todayAudio() {
        LocalDate currentDate = LocalDate.now();
        LambdaQueryWrapper<DailyAudioEntity> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(DailyAudioEntity::getDay, currentDate);
        DailyAudioEntity dailyAudioEntity = getOne(wrapper);

        if (dailyAudioEntity != null) {
            DailyAudioDto dailyAudioDto = BaseAssembler.convert(dailyAudioEntity, DailyAudioDto.class);
            List<VirtualGoodsDto> goods = listGoods(dailyAudioDto.getCategoryId());
            dailyAudioDto.setGoods(goods);
            return dailyAudioDto;
        } else {
            LocalDate yesterday = currentDate.minusDays(1L);
            wrapper = Wrappers.lambdaQuery();
            wrapper.eq(DailyAudioEntity::getDay, yesterday);
            DailyAudioEntity yesterdayAudio = getOne(wrapper);
            if (yesterdayAudio != null) {
                String categoryId = yesterdayAudio.getCategoryId();
                String audioId = yesterdayAudio.getAudioId();
                //尝试在昨天的音频基础上+1
                List<VirtualGoodsDto> goods = listGoods(categoryId);
                boolean yesterdayMatched = false;
                VirtualGoodsAudioEntity todayAudio = null;
                for (VirtualGoodsDto virtualGoodsDto : goods) {
                    if (todayAudio != null) {
                        break;
                    }
                    for (VirtualGoodsAudioEntity audioEntity : virtualGoodsDto.getAudioList()) {
                        if (yesterdayMatched) {
                            todayAudio = audioEntity;
                            break;
                        } else {
                            if (audioEntity.getAudioId().equals(audioId)) {
                                //找到昨天的音频了
                                yesterdayMatched = true;
                            }
                        }
                    }
                }

                if (todayAudio != null) {
                    dailyAudioEntity = new DailyAudioEntity();
                    dailyAudioEntity.setDay(currentDate);
                    dailyAudioEntity.setAudioId(todayAudio.getId());
                    dailyAudioEntity.setCategoryId(todayAudio.getCategoryId());
                    dailyAudioEntity.setGoodsId(todayAudio.getGoodsId());
                    save(dailyAudioEntity);
                }

                DailyAudioDto dailyAudioDto = BaseAssembler.convert(dailyAudioEntity, DailyAudioDto.class);
                dailyAudioDto.setGoods(goods);
                return dailyAudioDto;
            }

            DailyAudioDto dailyAudioDto = new DailyAudioDto();
            dailyAudioDto.setDay(currentDate);
            return dailyAudioDto;
        }
    }
}
