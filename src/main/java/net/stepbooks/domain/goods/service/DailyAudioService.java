package net.stepbooks.domain.goods.service;

import com.baomidou.mybatisplus.extension.service.IService;
import net.stepbooks.domain.goods.entity.DailyAudioEntity;
import net.stepbooks.interfaces.client.dto.DailyAudioDto;

public interface DailyAudioService extends IService<DailyAudioEntity> {

    DailyAudioDto todayAudio();

}
