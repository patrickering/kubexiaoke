package com.xiaoke.common.wx.entity;

import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * 接收模板消息对象
 *
 * @author xiaoke
 */
@Data
public class WxTemplate {

    /**
     * 通知类型
     */
    private String type;

    /**
     * 用户openId集合
     */
    private List<String> openIdList;

    /**
     * 用户openId
     */
    private String openId;
    /**
     * 用户Id集合
     */
    private List<String>  userIdList;

    /**
     * 跳转地址
     */
    private String url;

    /**
     * 参数
     */
    private List<Map<String, String>> dataList;

    /**
     * 参数
     */
    private Map<String, String> data;

    /**
     * 当前租户ID
     */
    private Integer tenantId;
    /**
     * 当前用户ID
     */
    private Integer userId;

    /**
     * 模板Id
     */
    private String templateId;

}
