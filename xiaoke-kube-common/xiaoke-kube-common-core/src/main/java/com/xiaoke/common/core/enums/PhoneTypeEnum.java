package com.xiaoke.common.core.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author xiaoke
 * @date 2019-05-16
 * <p>
 * 支付渠道
 */
@Getter
@AllArgsConstructor
public enum PhoneTypeEnum {
    LOGIN("login", "登录"),
    UPDATE_PHONE("updatePhone", "修改手机号"),
    FORGET_PASSWORD("forgetPassword", "忘记密码"),
    REGISTER("register", "注册");

    /**
     * 类型
     */
    private String type;

    /**
     * 描述
     */
    private String description;



    public static PhoneTypeEnum getByType(String type) {
        for (PhoneTypeEnum channelEnum : PhoneTypeEnum.values()) {
            if (channelEnum.getType().equals(type)) {
                return channelEnum;
            }
        }
        return null;
    }

}
