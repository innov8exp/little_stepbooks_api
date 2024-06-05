package net.stepbooks.domain.goods.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.stepbooks.domain.goods.entity.DailyAudioEntity;
import net.stepbooks.domain.goods.entity.VirtualCategoryEntity;
import net.stepbooks.domain.goods.entity.VirtualGoodsAudioEntity;
import net.stepbooks.domain.goods.mapper.DailyAudioMapper;
import net.stepbooks.domain.goods.service.DailyAudioService;
import net.stepbooks.domain.goods.service.VirtualCategoryService;
import net.stepbooks.domain.goods.service.VirtualGoodsService;
import net.stepbooks.infrastructure.assembler.BaseAssembler;
import net.stepbooks.interfaces.admin.dto.DailyAudioAdminDto;
import net.stepbooks.interfaces.client.dto.DailyAudioDto;
import net.stepbooks.interfaces.client.dto.VirtualGoodsDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class DailyAudioServiceImpl extends ServiceImpl<DailyAudioMapper, DailyAudioEntity>
        implements DailyAudioService {

    private final VirtualGoodsService virtualGoodsService;
    private final VirtualCategoryService virtualCategoryService;

    private List<VirtualGoodsDto> listGoods(String categoryId) {
        List<VirtualGoodsDto> goods = virtualGoodsService.listAll(categoryId);
        return goods;
    }

    @Transactional
    @Override
    public void set(DailyAudioEntity entity) {
        LambdaQueryWrapper<DailyAudioEntity> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(DailyAudioEntity::getDay, entity.getDay());
        DailyAudioEntity dailyAudioEntity = getOne(wrapper);
        if (dailyAudioEntity != null) {
            dailyAudioEntity.setAudioId(entity.getAudioId());
            dailyAudioEntity.setCategoryId(entity.getCategoryId());
            dailyAudioEntity.setGoodsId(entity.getGoodsId());
            updateById(dailyAudioEntity);
        } else {
            dailyAudioEntity = new DailyAudioEntity();
            dailyAudioEntity.setAudioId(entity.getAudioId());
            dailyAudioEntity.setCategoryId(entity.getCategoryId());
            dailyAudioEntity.setGoodsId(entity.getGoodsId());
            dailyAudioEntity.setDay(entity.getDay());
            save(dailyAudioEntity);
        }
    }

    private void fillinParentCategory(DailyAudioDto dailyAudioDto) {
        VirtualCategoryEntity virtualCategoryEntity = virtualCategoryService.getById(dailyAudioDto.getCategoryId());
        String parentId = virtualCategoryEntity.getParentId();
        if (parentId != null) {
            dailyAudioDto.setParentCategoryId(parentId);
        }
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
            fillinParentCategory(dailyAudioDto);
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
                            if (audioEntity.getId().equals(audioId)) {
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
                    DailyAudioDto dailyAudioDto = BaseAssembler.convert(dailyAudioEntity, DailyAudioDto.class);
                    fillinParentCategory(dailyAudioDto);
                    dailyAudioDto.setGoods(goods);
                    return dailyAudioDto;
                }
            }

            DailyAudioDto dailyAudioDto = new DailyAudioDto();
            dailyAudioDto.setDay(currentDate);
            return dailyAudioDto;
        }
    }

    private LocalDate calculateEndDay(List<VirtualGoodsDto> goods, DailyAudioEntity entity, LocalDate recentDay) {

        LocalDate day = entity.getDay();
        LocalDate endDay = day;
        boolean matched = false;

        for (VirtualGoodsDto virtualGoodsDto : goods) {
            for (VirtualGoodsAudioEntity audioEntity : virtualGoodsDto.getAudioList()) {
                if (matched) {
                    //找到之后，在不超过recentEndDay的基础上，每循环1次加1天
                    LocalDate tempDay = endDay.plusDays(1L);
                    if (recentDay == null || tempDay.isBefore(recentDay)) {
                        endDay = tempDay;
                    } else {
                        return endDay;
                    }

                } else if (audioEntity.getId().equals(entity.getAudioId())) {
                    //找到了
                    matched = true;
                }
            }
        }
        return endDay;
    }

    @Override
    public IPage<DailyAudioAdminDto> list(int currentPage, int pageSize) {
        Page<DailyAudioEntity> page = Page.of(currentPage, pageSize);
        LambdaQueryWrapper<DailyAudioEntity> wrapper = Wrappers.lambdaQuery();
        wrapper.orderByDesc(DailyAudioEntity::getDay);

        IPage<DailyAudioEntity> entities = page(page, wrapper);

        IPage<DailyAudioAdminDto> results = new Page<>();
        results.setCurrent(entities.getCurrent());
        results.setPages(entities.getPages());
        results.setTotal(entities.getTotal());

        List<DailyAudioAdminDto> records = new ArrayList<>();
        results.setRecords(records);

        LocalDate recentDay = null;
        for (DailyAudioEntity entity : entities.getRecords()) {
            DailyAudioAdminDto dto = BaseAssembler.convert(entity, DailyAudioAdminDto.class);
            if (currentPage <= 1) {
                List<VirtualGoodsDto> goods = listGoods(dto.getCategoryId());
                LocalDate endDay = calculateEndDay(goods, entity, recentDay);
                dto.setEndDay(endDay);
                recentDay = dto.getDay();
            }
            records.add(dto);
        }

        return results;
    }
}
