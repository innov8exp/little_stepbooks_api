package net.stepbooks.domain.points.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import net.stepbooks.domain.points.entity.PointsRule;
import net.stepbooks.domain.points.enums.PointsEventType;
import net.stepbooks.domain.points.mapper.PointsRuleMapper;
import net.stepbooks.domain.points.service.PointsRuleService;
import org.springframework.stereotype.Service;

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
}
