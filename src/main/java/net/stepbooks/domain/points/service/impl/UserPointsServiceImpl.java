package net.stepbooks.domain.points.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import net.stepbooks.domain.points.entity.UserPoints;
import net.stepbooks.domain.points.entity.UserPointsLog;
import net.stepbooks.domain.points.enums.PointsEventType;
import net.stepbooks.domain.points.mapper.UserPointsMapper;
import net.stepbooks.domain.points.service.UserPointsLogService;
import net.stepbooks.domain.points.service.UserPointsService;
import net.stepbooks.interfaces.client.dto.PointsDto;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@RequiredArgsConstructor
@Service
public class UserPointsServiceImpl extends ServiceImpl<UserPointsMapper, UserPoints>
        implements UserPointsService {

    private final UserPointsLogService userPointsLogService;

    /**
     * 重新计算用户的总积分
     *
     * @param userId
     * @param thisYearsNewYear
     * @param nextYearsNewYear
     * @return
     */
    private UserPoints calculate(String userId, LocalDate thisYearsNewYear, LocalDate nextYearsNewYear) {
        UserPoints userPoints = getUserPointsByUserId(userId);

        int totalPoints = userPointsLogService.pointsTotal(userId, thisYearsNewYear, nextYearsNewYear);

        if (ObjectUtils.isEmpty(userPoints)) {
            userPoints = new UserPoints();
            userPoints.setUserId(userId);
            userPoints.setExpireAt(nextYearsNewYear);
            userPoints.setPoints(totalPoints);
            save(userPoints);
        } else {
            userPoints.setExpireAt(nextYearsNewYear);
            userPoints.setPoints(totalPoints);
            updateById(userPoints);
        }

        return userPoints;
    }

    @Override
    public PointsDto dailyCheckin(String userId, int continuesDay) {

        int pointsChange = 1;
        String reason = "每日登录积分奖励";

        LocalDate thisYearsNewYear = LocalDate.now().withMonth(1).withDayOfMonth(1);
        LocalDate nextYearsNewYear = thisYearsNewYear.plusYears(1);

        PointsDto pointsDto = new PointsDto();
        pointsDto.setAmount(pointsChange);
        pointsDto.setReason(reason);

        UserPointsLog userPointsLog = new UserPointsLog();
        userPointsLog.setEventType(PointsEventType.DAILY_CHECK_IN);
        userPointsLog.setUserId(userId);
        userPointsLog.setPointsChange(pointsChange);
        userPointsLog.setReason(reason);
        userPointsLog.setExpireAt(nextYearsNewYear);
        userPointsLogService.save(userPointsLog);

        UserPoints userPoints = calculate(userId, thisYearsNewYear, nextYearsNewYear);
        pointsDto.setTotalAmount(userPoints.getPoints());

        return pointsDto;
    }

    @Override
    public UserPoints getUserPointsByUserId(String userId) {
        LambdaQueryWrapper<UserPoints> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(UserPoints::getUserId, userId);
        UserPoints userPoints = getOne(wrapper);
        return userPoints;
    }
}
