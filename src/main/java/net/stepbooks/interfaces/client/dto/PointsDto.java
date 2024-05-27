package net.stepbooks.interfaces.client.dto;

import lombok.Data;

/**
 * 积分变化信息
 */

@Data
public class PointsDto {

    /**
     * 本次积分变化数量
     */
    private Integer amount;

    /**
     * 本次积分变化原因
     */
    private String reason;

    /**
     * 当前用户的总积分
     */
    private Integer totalAmount;
}
