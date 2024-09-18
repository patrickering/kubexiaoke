package com.xiaoke.common.core.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 自定义异常枚举
 */
@NoArgsConstructor
@AllArgsConstructor
public enum ResultStatusEnum {
    /**
     * 未授权，请登录
     */
    NOT_LOGIN(401, "未授权，请登录"),

    /**
     * 没有权限
     */
    NOT_AUTH(403, "拒绝访问"),

    /**
     *缺少租户ID
     */
    NO_TENANT(405, "缺少租户ID"),

    /**
     * 重复访问
     */
    REPEAT_VISIT(506, "重复访问"),

    /**
     * 缺少请求头
     */
    NO_IDEMPOTENT_HEADER(400, "缺少幂等请求头");

    @Getter
    @Setter
    private int code;

    @Getter
    @Setter
    private String message;
}