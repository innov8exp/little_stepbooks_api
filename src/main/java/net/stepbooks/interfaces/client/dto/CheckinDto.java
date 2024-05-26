package net.stepbooks.interfaces.client.dto;

import lombok.Data;

@Data
public class CheckinDto {

    /**
     * 积分变化
     */
    private PointsDto points;

    /**
     * 连续签到天数
     */
    private Integer continuesDay;

}
