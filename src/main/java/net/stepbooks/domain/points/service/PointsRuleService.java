package net.stepbooks.domain.points.service;

import com.baomidou.mybatisplus.extension.service.IService;
import net.stepbooks.domain.points.entity.PointsRule;
import net.stepbooks.domain.points.enums.PointsEventType;

public interface PointsRuleService extends IService<PointsRule> {

    PointsRule getRuleByType(PointsEventType type);

}
