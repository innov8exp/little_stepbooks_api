package net.stepbooks.domain.points.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.stepbooks.domain.points.entity.PointsRule;
import net.stepbooks.domain.points.entity.UserPoints;
import net.stepbooks.domain.points.entity.UserPointsLog;
import net.stepbooks.domain.points.enums.PointsEventType;
import net.stepbooks.domain.points.mapper.UserPointsMapper;
import net.stepbooks.domain.points.service.PointsRuleService;
import net.stepbooks.domain.points.service.UserPointsLogService;
import net.stepbooks.domain.points.service.UserPointsService;
import net.stepbooks.infrastructure.AppConstants;
import net.stepbooks.interfaces.client.dto.PointsDto;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserPointsServiceImpl extends ServiceImpl<UserPointsMapper, UserPoints>
        implements UserPointsService {

    private final UserPointsLogService userPointsLogService;
    private final PointsRuleService pointsRuleService;

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

    /**
     * 活动签到，如果不在活动期间，或者活动期间已经签到，则返回空
     *
     * @param userId
     * @return
     */
    private PointsDto activityCheckin(String userId) {

        PointsRule pointsRule = pointsRuleService.getRuleByType(PointsEventType.ACTIVITY_CHECK_IN);
        if (pointsRule == null
                || pointsRule.getPoints() <= 0) {
            //没有活动签到
            return null;
        }
        LocalDate today = LocalDate.now();
        LocalDate activityStartDay = pointsRule.getActivityStartDay();
        if (activityStartDay != null && today.isBefore(activityStartDay)) {
            //没到开始时间
            return null;
        }

        LocalDate activityEndDay = pointsRule.getActivityEndDay();
        if (activityEndDay != null && today.isAfter(activityEndDay)) {
            //活动已结束
            return null;
        }

        //检查用户在活动期间是否已经拿过积分了
        LambdaQueryWrapper<UserPointsLog> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(UserPointsLog::getEventType, PointsEventType.ACTIVITY_CHECK_IN);
        wrapper.eq(UserPointsLog::getUserId, userId);
        if (activityStartDay != null) {
            wrapper.ge(UserPointsLog::getCreatedAt, activityStartDay);
        }
        if (activityEndDay != null) {
            wrapper.le(UserPointsLog::getCreatedAt, activityEndDay);
        }
        if (userPointsLogService.exists(wrapper)) {
            //用户已经在活动期间签到过
            log.info("user {} got activity points already", userId);
            return null;
        }

        log.info("add activity points for user {}", userId);

        return addPointsImpl(userId, pointsRule);
    }

    @Override
    public PointsDto dailyCheckin(String userId, int continuesDay) {

        PointsDto activityPointsDto = activityCheckin(userId);

        if (activityPointsDto != null) {
            //优先尝试活动签到，如果活动签到成功，那么就不再进行每日签到
            return activityPointsDto;
        }

        PointsRule pointsRule = null;
        if (continuesDay >= AppConstants.THIRTY_DAYS) {
            pointsRule = pointsRuleService.getRuleByType(PointsEventType.CHECK_IN_30_DAY);
            if (pointsRule == null || pointsRule.getPoints() <= 0) {
                pointsRule = pointsRuleService.getRuleByType(PointsEventType.CHECK_IN_7_DAY);

            }
        } else if (continuesDay >= AppConstants.SEVEN_DAYS) {
            pointsRule = pointsRuleService.getRuleByType(PointsEventType.CHECK_IN_7_DAY);
        }

        if (pointsRule == null || pointsRule.getPoints() <= 0) {
            pointsRule = pointsRuleService.getRuleByType(PointsEventType.DAILY_CHECK_IN);
        }

        if (pointsRule == null) {
            return null;
        }

        return addPointsImpl(userId, pointsRule);
    }

    private PointsDto addPointsImpl(String userId, PointsRule pointsRule) {

        int pointsChange = pointsRule.getPoints();
        String reason = pointsRule.getReason();

        LocalDate thisYearsNewYear = LocalDate.now().withMonth(1).withDayOfMonth(1);
        LocalDate nextYearsNewYear = thisYearsNewYear.plusYears(1);

        PointsDto pointsDto = new PointsDto();
        pointsDto.setAmount(pointsChange);
        pointsDto.setReason(reason);

        UserPointsLog userPointsLog = new UserPointsLog();
        userPointsLog.setEventType(pointsRule.getEventType());
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
