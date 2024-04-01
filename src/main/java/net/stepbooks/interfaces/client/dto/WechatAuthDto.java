package net.stepbooks.interfaces.client.dto;

import lombok.Data;


@Data
public class WechatAuthDto {
    private String code;

    /**
     * 微信昵称
     */
    private String nickname;

    /**
     * 微信头像
     */
    private String avatarUrl;

    /**
     * 是否手机号登录
     */
    private Boolean isPhoneLogin;
}
