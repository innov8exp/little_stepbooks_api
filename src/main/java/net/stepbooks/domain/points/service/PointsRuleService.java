package net.stepbooks.domain.points.service;

import com.baomidou.mybatisplus.extension.service.IService;
import net.stepbooks.domain.points.entity.PointsRule;
import net.stepbooks.domain.points.enums.PointsEventType;
import net.stepbooks.interfaces.admin.dto.FullPointsRuleDto;

public interface PointsRuleService extends IService<PointsRule> {

    PointsRule getRuleByType(PointsEventType type);

    void setFullPointsRule(FullPointsRuleDto fullPointsRule);

    FullPointsRuleDto getFullPointsRule();

}
