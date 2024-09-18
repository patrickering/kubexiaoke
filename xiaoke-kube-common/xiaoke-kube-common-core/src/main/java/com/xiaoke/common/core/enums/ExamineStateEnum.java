package com.xiaoke.common.core.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author xiaoke
 * @date 2022年11月29日 12:08:26
 * <p>
 * 审核状态
 */
@Getter
@AllArgsConstructor
public enum ExamineStateEnum {
    /**
     * 审核中
     */
    WAIT("10", "审核中"),
    /**
     * 审核通过
     */
    ADOPT("20", "审核通过"),
    /**
     * 审核驳回
     */
    REJECT("30", "审核驳回");

    /**
     * 状态
     */
    private String state;
    /**
     * 描述
     */
    private String description;
}
