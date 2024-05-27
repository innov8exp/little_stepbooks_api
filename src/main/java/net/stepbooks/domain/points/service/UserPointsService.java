package net.stepbooks.domain.points.service;

import com.baomidou.mybatisplus.extension.service.IService;
import net.stepbooks.domain.points.entity.UserPoints;
import net.stepbooks.interfaces.client.dto.PointsDto;

public interface UserPointsService extends IService<UserPoints> {
    PointsDto dailyCheckin(String userId, int continuesDay);
}
