package net.stepbooks.domain.points.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.stepbooks.domain.order.entity.Order;
import net.stepbooks.domain.order.service.OrderSkuService;
import net.stepbooks.domain.points.entity.PointsRule;
import net.stepbooks.domain.points.entity.UserPoints;
import net.stepbooks.domain.points.entity.UserPointsLog;
import net.stepbooks.domain.points.enums.PointsEventType;
import net.stepbooks.domain.points.enums.PointsStatus;
import net.stepbooks.domain.points.mapper.UserPointsMapper;
import net.stepbooks.domain.points.service.PointsRuleService;
import net.stepbooks.domain.points.service.UserPointsLogService;
import net.stepbooks.domain.points.service.UserPointsService;
import net.stepbooks.domain.product.entity.Sku;
import net.stepbooks.infrastructure.AppConstants;
import net.stepbooks.interfaces.client.dto.PointsDto;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserPointsServiceImpl extends ServiceImpl<UserPointsMapper, UserPoints>
        implements UserPointsService {

    private static final String ALL = "*";

    private final UserPointsLogService userPointsLogService;
    private final PointsRuleService pointsRuleService;
    private final OrderSkuService orderSkuService;

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

        return addPointsImpl(userId, pointsRule, PointsStatus.CONFIRMED);
    }

    @Override
    public PointsDto dailyCheckin(String userId, int continuesDay) {

        PointsDto activityPointsDto = activityCheckin(userId);

        if (activityPointsDto != null) {
            //优先尝试活动签到，如果活动签到成功，那么就不再进行每日签到
            return activityPointsDto;
        }

        PointsRule pointsRule = null;
        if (continuesDay >= AppConstants.SEVEN_DAYS) {
            pointsRule = pointsRuleService.getRuleByType(PointsEventType.CHECK_IN_7_DAY);
            if (pointsRule == null || pointsRule.getPoints() <= 0) {
                pointsRule = pointsRuleService.getRuleByType(PointsEventType.CHECK_IN_3_DAY);

            }
        } else if (continuesDay >= AppConstants.THREE_DAYS) {
            pointsRule = pointsRuleService.getRuleByType(PointsEventType.CHECK_IN_3_DAY);
        }

        if (pointsRule == null || pointsRule.getPoints() <= 0) {
            pointsRule = pointsRuleService.getRuleByType(PointsEventType.DAILY_CHECK_IN);
        }

        if (pointsRule == null) {
            return null;
        }

        return addPointsImpl(userId, pointsRule, PointsStatus.CONFIRMED);
    }

    @Override
    public void orderPaid(Order order) {
        try {
            String userId = order.getUserId();
            String orderId = order.getId();

            LambdaQueryWrapper<UserPointsLog> wrapper = Wrappers.lambdaQuery();
            wrapper.eq(UserPointsLog::getUserId, userId);
            wrapper.eq(UserPointsLog::getOrderId, orderId);

            if (userPointsLogService.exists(wrapper)) {
                return;
            }

            int yuan = order.getPaymentAmount().intValue();
            if (yuan <= 0) {
                return;
            }
            PointsRule pointsRule = pointsRuleService.getRuleByType(PointsEventType.BUY_PROMOTION_PRODUCT);
            boolean usePromotionRule = false;
            if (pointsRule != null && pointsRule.inActivePeriod()) {
                String spus = pointsRule.getSpus();
                if (ALL.equals(spus)) {
                    //全场促销
                    usePromotionRule = true;
                } else {
                    //只针对特殊商品促销
                    List<Sku> skus = orderSkuService.findSkusByOrderId(orderId);
                    for (Sku sku : skus) {
                        if (spus.contains(sku.getSpuId())) {
                            usePromotionRule = true;
                            break;
                        }
                    }
                }
            }
            if (!usePromotionRule) {
                //使用正常的消费积分兑换
                pointsRule = pointsRuleService.getRuleByType(PointsEventType.BUY_NORMAL_PRODUCT);
            }

            addPointsImpl(userId, pointsRule.getPoints() * yuan,
                    pointsRule.getReason(), pointsRule.getEventType(), PointsStatus.PENDING);

        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    @Override
    public void orderSigned(Order order) {
        try {
            LocalDate thisYearsNewYear = LocalDate.now().withMonth(1).withDayOfMonth(1);
            LocalDate nextYearsNewYear = thisYearsNewYear.plusYears(1);

            LambdaQueryWrapper<UserPointsLog> wrapper = Wrappers.lambdaQuery();
            wrapper.eq(UserPointsLog::getUserId, order.getUserId());
            wrapper.eq(UserPointsLog::getOrderId, order.getId());
            wrapper.eq(UserPointsLog::getStatus, PointsStatus.PENDING);

            UserPointsLog userPointsLog = userPointsLogService.getOne(wrapper);
            userPointsLog.setStatus(PointsStatus.CONFIRMED);
            userPointsLog.setExpireAt(nextYearsNewYear);
            userPointsLogService.updateById(userPointsLog);
            calculate(order.getUserId(), thisYearsNewYear, nextYearsNewYear);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    @Override
    public void orderRefund(Order order) {
        try {
            LocalDate thisYearsNewYear = LocalDate.now().withMonth(1).withDayOfMonth(1);
            LocalDate nextYearsNewYear = thisYearsNewYear.plusYears(1);

            LambdaQueryWrapper<UserPointsLog> wrapper = Wrappers.lambdaQuery();
            wrapper.eq(UserPointsLog::getUserId, order.getUserId());
            wrapper.eq(UserPointsLog::getOrderId, order.getId());
            UserPointsLog userPointsLog = userPointsLogService.getOne(wrapper);
            userPointsLog.setStatus(PointsStatus.INVALID);
            userPointsLogService.updateById(userPointsLog);
            calculate(order.getUserId(), thisYearsNewYear, nextYearsNewYear);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    private PointsDto addPointsImpl(String userId, PointsRule pointsRule, PointsStatus status) {
        return addPointsImpl(userId, pointsRule.getPoints(),
                pointsRule.getReason(), pointsRule.getEventType(), status);
    }

    private PointsDto addPointsImpl(String userId, int pointsChange, String reason,
                                    PointsEventType eventType, PointsStatus status) {

        LocalDate thisYearsNewYear = LocalDate.now().withMonth(1).withDayOfMonth(1);
        LocalDate nextYearsNewYear = thisYearsNewYear.plusYears(1);

        PointsDto pointsDto = new PointsDto();
        pointsDto.setAmount(pointsChange);
        pointsDto.setReason(reason);

        UserPointsLog userPointsLog = new UserPointsLog();
        userPointsLog.setStatus(status);
        userPointsLog.setEventType(eventType);
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
