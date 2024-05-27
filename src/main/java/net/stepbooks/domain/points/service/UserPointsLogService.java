package net.stepbooks.domain.points.service;

import com.baomidou.mybatisplus.extension.service.IService;
import net.stepbooks.domain.points.entity.UserPointsLog;

import java.time.LocalDate;

public interface UserPointsLogService extends IService<UserPointsLog> {

    /**
     * 计算用户一年的积分总和
     *
     * @param userId
     * @param thisYearsNewYear
     * @param nextYearsNewYear
     * @return
     */
    int pointsTotal(String userId, LocalDate thisYearsNewYear, LocalDate nextYearsNewYear);

}
