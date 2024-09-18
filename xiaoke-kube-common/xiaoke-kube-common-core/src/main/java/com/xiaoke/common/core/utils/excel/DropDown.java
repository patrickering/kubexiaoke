package com.xiaoke.common.core.utils.excel;

import lombok.Data;

/**
 * 下拉数据对象
 */
@Data
public class DropDown {
    private DropDown() {
    }
    public DropDown(Integer index, String[] data) {
        this.index = index;
        this.data = data;
    }

    /**
     * 设置列
     */
    private Integer index;

    /**
     * 下拉数据
     */
    private String[] data;
}
