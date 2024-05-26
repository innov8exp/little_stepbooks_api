package net.stepbooks.domain.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import net.stepbooks.domain.user.entity.UserCheckinLog;
import net.stepbooks.interfaces.client.dto.CheckinDto;

public interface UserCheckinLogService extends IService<UserCheckinLog> {

    /**
     * 每日签到，如果已签到则抛出异常
     *
     * @param userId
     * @return
     */
    CheckinDto dailyCheckin(String userId);

}
