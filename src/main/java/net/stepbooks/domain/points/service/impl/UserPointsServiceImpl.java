package net.stepbooks.domain.points.service.impl;

import net.stepbooks.domain.points.service.UserPointsService;
import net.stepbooks.interfaces.client.dto.PointsDto;
import org.springframework.stereotype.Service;

@Service
public class UserPointsServiceImpl implements UserPointsService {
    @Override
    public PointsDto checkin(String userId) {
        PointsDto pointsDto = new PointsDto();
        pointsDto.setAmount(1);
        pointsDto.setReason("每日登录积分奖励"); //TODO 多语言支持
        return pointsDto;
    }
}
