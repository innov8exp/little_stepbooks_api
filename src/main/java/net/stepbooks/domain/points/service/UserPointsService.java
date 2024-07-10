package net.stepbooks.domain.points.service;

import com.baomidou.mybatisplus.extension.service.IService;
import net.stepbooks.domain.points.entity.UserPoints;
import net.stepbooks.interfaces.client.dto.PointsDto;

public interface UserPointsService extends IService<UserPoints> {
    PointsDto dailyCheckin(String userId, int continuesDay);

    PointsDto orderPaid(String userId, String orderId, String productId, int yuan);

    PointsDto orderSigned(String orderId);

    UserPoints getUserPointsByUserId(String userId);
}
