package net.stepbooks.domain.points.service.impl;

import lombok.RequiredArgsConstructor;
import net.stepbooks.domain.points.entity.UserPointsLog;
import net.stepbooks.domain.points.enums.PointsEventType;
import net.stepbooks.domain.points.service.UserPointsLogService;
import net.stepbooks.domain.points.service.UserPointsService;
import net.stepbooks.interfaces.client.dto.PointsDto;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@RequiredArgsConstructor
@Service
public class UserPointsServiceImpl implements UserPointsService {

    private final UserPointsLogService userPointsLogService;

    /**
     * 积分于明年1月1日过期
     *
     * @return
     */
    private LocalDate nextYearsNewYear() {
        LocalDate nextYearsNewYear = LocalDate.now().plusYears(1).withMonth(1).withDayOfMonth(1);
        return nextYearsNewYear;
    }

    @Override
    public PointsDto dailyCheckin(String userId, int continuesDay) {

        int pointsChange = 1;
        String reason = "每日登录积分奖励";

        PointsDto pointsDto = new PointsDto();
        pointsDto.setAmount(pointsChange);
        pointsDto.setReason(reason);

        UserPointsLog userPointsLog = new UserPointsLog();
        userPointsLog.setEventType(PointsEventType.DAILY_CHECK_IN);
        userPointsLog.setUserId(userId);
        userPointsLog.setPointsChange(pointsChange);
        userPointsLog.setReason(reason);
        userPointsLog.setExpireAt(nextYearsNewYear());
        userPointsLogService.save(userPointsLog);
        return pointsDto;
    }
}
