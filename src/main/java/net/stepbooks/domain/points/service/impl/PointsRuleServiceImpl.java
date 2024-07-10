package net.stepbooks.domain.points.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import net.stepbooks.domain.points.entity.PointsRule;
import net.stepbooks.domain.points.enums.PointsEventType;
import net.stepbooks.domain.points.mapper.PointsRuleMapper;
import net.stepbooks.domain.points.service.PointsRuleService;
import net.stepbooks.interfaces.admin.dto.FullPointsRuleDto;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class PointsRuleServiceImpl extends ServiceImpl<PointsRuleMapper, PointsRule>
        implements PointsRuleService {

    @Override
    public PointsRule getRuleByType(PointsEventType type) {
        LambdaQueryWrapper<PointsRule> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(PointsRule::getEventType, type);
        wrapper.eq(PointsRule::isActive, true);
        PointsRule rule = getOne(wrapper);
        return rule;
    }

    private void checkParam(FullPointsRuleDto fullPointsRule) {
    }

    @Override
    public void setFullPointsRule(FullPointsRuleDto fullPointsRule) {
        checkParam(fullPointsRule);
    }

    private void fillinSpus(PointsRule rule, FullPointsRuleDto fullPointsRule) {
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
