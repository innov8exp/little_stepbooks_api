package net.stepbooks.domain.goods.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import net.stepbooks.domain.goods.entity.DailyAudioEntity;
import net.stepbooks.interfaces.admin.dto.DailyAudioAdminDto;
import net.stepbooks.interfaces.client.dto.DailyAudioDto;

public interface DailyAudioService extends IService<DailyAudioEntity> {

    void set(DailyAudioEntity entity);

    DailyAudioDto todayAudio();

    IPage<DailyAudioAdminDto> list(int currentPage, int pageSize);
}
