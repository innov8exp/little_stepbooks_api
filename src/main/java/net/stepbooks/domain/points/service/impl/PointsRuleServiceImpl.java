package net.stepbooks.domain.points.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import net.stepbooks.domain.points.entity.PointsRule;
import net.stepbooks.domain.points.enums.PointsEventType;
import net.stepbooks.domain.points.mapper.PointsRuleMapper;
import net.stepbooks.domain.points.service.PointsRuleService;
import net.stepbooks.domain.product.entity.Product;
import net.stepbooks.domain.product.service.ProductService;
import net.stepbooks.infrastructure.exception.BusinessException;
import net.stepbooks.infrastructure.exception.ErrorCode;
import net.stepbooks.interfaces.admin.dto.FullPointsRuleDto;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

@RequiredArgsConstructor
@Service
public class PointsRuleServiceImpl extends ServiceImpl<PointsRuleMapper, PointsRule>
        implements PointsRuleService {

    private final ProductService productService;

    private static final String ALL = "*";

    private static final int MAX_CHECK_IN_POINT = 9;

    private static final int MAX_POINTS_PER_YUAN = 5;

    @Override
    public PointsRule getRuleByType(PointsEventType type) {
        LambdaQueryWrapper<PointsRule> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(PointsRule::getEventType, type);
        wrapper.eq(PointsRule::isActive, true);
        PointsRule rule = getOne(wrapper);
        return rule;
    }

    private void checkParam(FullPointsRuleDto fullPointsRule) {
        if (fullPointsRule.getDailyCheckInPoints() <= 0
                || fullPointsRule.getThreeDayCheckInPoints() <= 0
                || fullPointsRule.getSevenDayCheckInPoints() <= 0) {
            throw new BusinessException(ErrorCode.BAD_REQUEST, "签到积分不能为0");
        }

        if (fullPointsRule.getDailyCheckInPoints() > MAX_CHECK_IN_POINT
                || fullPointsRule.getThreeDayCheckInPoints() > MAX_CHECK_IN_POINT
                || fullPointsRule.getSevenDayCheckInPoints() > MAX_CHECK_IN_POINT) {
            throw new BusinessException(ErrorCode.BAD_REQUEST, "签到积分设置过大");
        }

        if (fullPointsRule.isSpecialCheckIn()) {
            if (fullPointsRule.getSpecialCheckInPoints() <= 0) {
                throw new BusinessException(ErrorCode.BAD_REQUEST, "节假日签到积分不能为0");
            }
            if (fullPointsRule.getActivityEndDay() == null) {
                throw new BusinessException(ErrorCode.BAD_REQUEST, "必须设置节假日签到的结束时间");
            }
        }

        if (fullPointsRule.getPointsPerYuanNormal() != 1) {
            throw new BusinessException(ErrorCode.BAD_REQUEST, "正常消费1元兑换积分固定为1积分");
        }

        if (fullPointsRule.isPointsPromotion()) {

            if (fullPointsRule.getPointsPerYuanPromotion() <= 0) {
                throw new BusinessException(ErrorCode.BAD_REQUEST, "请设置促销活动消费1元可兑换积分");
            }

            if (fullPointsRule.getPointsPerYuanPromotion() > MAX_POINTS_PER_YUAN) {
                throw new BusinessException(ErrorCode.BAD_REQUEST, "消费1元可兑换积分太多了，请设置小一点");
            }

            if (fullPointsRule.getPromotionEndDay() == null) {
                throw new BusinessException(ErrorCode.BAD_REQUEST, "必须设置促销活动的结束时间");
            }

            if (ObjectUtils.isEmpty(fullPointsRule.getSpuIds())) {
                throw new BusinessException(ErrorCode.BAD_REQUEST, "请设置促销商品");
            }
        }
    }

    @Override
    public void setFullPointsRule(FullPointsRuleDto fullPointsRule) {
        checkParam(fullPointsRule);

        List<PointsRule> toBeUpdated = new ArrayList<>();
        List<PointsRule> ruleList = list();
        for (PointsRule rule : ruleList) {
            if (PointsEventType.DAILY_CHECK_IN.equals(rule.getEventType())) {
                Integer points = fullPointsRule.getDailyCheckInPoints();
                if (!points.equals(rule.getPoints())) {
                    rule.setPoints(points);
                    toBeUpdated.add(rule);
                }
            } else if (PointsEventType.CHECK_IN_3_DAY.equals(rule.getEventType())) {
                Integer points = fullPointsRule.getThreeDayCheckInPoints();
                if (!points.equals(rule.getPoints())) {
                    rule.setPoints(points);
                    toBeUpdated.add(rule);
                }
            } else if (PointsEventType.CHECK_IN_7_DAY.equals(rule.getEventType())) {
                Integer points = fullPointsRule.getSevenDayCheckInPoints();
                if (!points.equals(rule.getPoints())) {
                    rule.setPoints(points);
                    toBeUpdated.add(rule);
                }
            } else if (PointsEventType.ACTIVITY_CHECK_IN.equals(rule.getEventType())) {
                if (fullPointsRule.isSpecialCheckIn()) {
                    Integer points = fullPointsRule.getSpecialCheckInPoints();
                    if (!rule.isActive()
                            || !points.equals(rule.getPoints())
                            || !rule.getActivityStartDay().equals(fullPointsRule.getActivityStartDay())
                            || !rule.getActivityEndDay().equals(fullPointsRule.getActivityEndDay())) {
                        rule.setActive(true);
                        rule.setPoints(points);
                        rule.setActivityStartDay(fullPointsRule.getActivityStartDay());
                        rule.setActivityEndDay(fullPointsRule.getActivityEndDay());
                        toBeUpdated.add(rule);
                    }
                } else if (rule.isActive()) {
                    rule.setActive(false);
                    toBeUpdated.add(rule);
                }
            } else if (PointsEventType.BUY_NORMAL_PRODUCT.equals(rule.getEventType())) {
                Integer points = fullPointsRule.getPointsPerYuanNormal();
                if (!points.equals(rule.getPoints())) {
                    rule.setPoints(points);
                    toBeUpdated.add(rule);
                }
            } else if (PointsEventType.BUY_PROMOTION_PRODUCT.equals(rule.getEventType())) {
                if (fullPointsRule.isPointsPromotion()) {
                    Integer points = fullPointsRule.getPointsPerYuanPromotion();
                    if (!rule.isActive()
                            || !points.equals(rule.getPoints())
                            || !rule.getActivityStartDay().equals(fullPointsRule.getPromotionStartDay())
                            || !rule.getActivityEndDay().equals(fullPointsRule.getPromotionEndDay())) {
                        rule.setActive(true);
                        rule.setPoints(points);
                        rule.setActivityStartDay(fullPointsRule.getPromotionStartDay());
                        rule.setActivityEndDay(fullPointsRule.getPromotionEndDay());
                        toBeUpdated.add(rule);
                    }
                } else if (rule.isActive()) {
                    rule.setActive(false);
                    toBeUpdated.add(rule);
                }

            }
        }

        if (toBeUpdated.size() > 0) {
            updateBatchById(toBeUpdated);
        }

    }

    private void fillinSpus(PointsRule rule, FullPointsRuleDto fullPointsRule) {
        String spus = rule.getSpus();
        fullPointsRule.setSpuIds(spus);
        if (spus != null && !ALL.equals(spus)) {
            List<String> spuIds = new ArrayList<>();
            StringTokenizer st = new StringTokenizer(spus, ",");
            while (st.hasMoreTokens()) {
                String skuId = st.nextToken();
                spuIds.add(skuId);
            }
            List<Product> products = productService.listByIds(spuIds);
            fullPointsRule.setSpus(products);
        }
    }

    @Override
    public FullPointsRuleDto getFullPointsRule() {
        FullPointsRuleDto fullPointsRule = new FullPointsRuleDto();
        List<PointsRule> ruleList = list();
        for (PointsRule rule : ruleList) {
            if (rule.isActive()) {
                if (PointsEventType.DAILY_CHECK_IN.equals(rule.getEventType())) {
                    fullPointsRule.setDailyCheckInPoints(rule.getPoints());
                } else if (PointsEventType.CHECK_IN_3_DAY.equals(rule.getEventType())) {
                    fullPointsRule.setThreeDayCheckInPoints(rule.getPoints());
                } else if (PointsEventType.CHECK_IN_7_DAY.equals(rule.getEventType())) {
                    fullPointsRule.setSevenDayCheckInPoints(rule.getPoints());
                } else if (PointsEventType.ACTIVITY_CHECK_IN.equals(rule.getEventType())) {
                    fullPointsRule.setSpecialCheckIn(true);
                    fullPointsRule.setSpecialCheckInPoints(rule.getPoints());
                    fullPointsRule.setActivityStartDay(rule.getActivityStartDay());
                    fullPointsRule.setActivityEndDay(rule.getActivityEndDay());
                } else if (PointsEventType.BUY_NORMAL_PRODUCT.equals(rule.getEventType())) {
                    fullPointsRule.setPointsPerYuanNormal(rule.getPoints());
                } else if (PointsEventType.BUY_PROMOTION_PRODUCT.equals(rule.getEventType())) {
                    fullPointsRule.setPointsPromotion(true);
                    fullPointsRule.setPointsPerYuanPromotion(rule.getPoints());
                    fullPointsRule.setPromotionStartDay(rule.getActivityStartDay());
                    fullPointsRule.setPromotionEndDay(rule.getActivityEndDay());
                    fillinSpus(rule, fullPointsRule);
                }
            }
        }
        return fullPointsRule;
    }
}
