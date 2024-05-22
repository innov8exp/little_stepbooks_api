package net.stepbooks.interfaces.client.dto;

import lombok.Data;

/**
 * 积分变化信息
 */

@Data
public class PointsDto {

    /**
     * 积分变化数量
     */
    private Integer amount;

    /**
     * 积分变化原因
     */
    private String reason;
}
