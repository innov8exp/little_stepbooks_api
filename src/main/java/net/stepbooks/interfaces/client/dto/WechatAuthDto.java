package net.stepbooks.interfaces.client.dto;

import lombok.Data;


@Data
public class WechatAuthDto {
    private String code;
    private String openId;
}
