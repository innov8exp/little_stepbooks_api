package net.stepbooks.interfaces.client.dto;

import lombok.Data;


@Data
public class WechatAuthDto {
    private String code;
    /**
     * 是否手机号登录
     */
    private Boolean isPhoneLogin;
}
