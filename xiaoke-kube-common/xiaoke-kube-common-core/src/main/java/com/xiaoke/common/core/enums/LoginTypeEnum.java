package com.xiaoke.common.core.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author xiaoke
 * @date 2019-05-16
 * <p>
 * 登录类型
 */
@Getter
@AllArgsConstructor
public enum LoginTypeEnum {
    /**
     * 登录
     */
    LOGIN("LOGIN", "登录"),
    /**
     * 登出
     */
    OUT("OUT", "登出");
    /**
     * 类型
     */
    private String type;
    /**
     * 描述
     */
    private String description;
}
