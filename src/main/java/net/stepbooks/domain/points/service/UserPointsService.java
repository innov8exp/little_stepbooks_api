package net.stepbooks.domain.points.service;

import net.stepbooks.interfaces.client.dto.PointsDto;

public interface UserPointsService {
    PointsDto checkin(String userId);
}
