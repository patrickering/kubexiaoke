package com.xiaoke.entity.kube.entity;

import lombok.Data;

import java.util.Map;

@Data
public class Content {
    /**
     * 消息类型
     * appList  应用列表
     */
    private String type;

    /**
     * 内容
     */
    private Map<String,Object> content;
}
