package com.xiaoke.entity.kube.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class SockMessage {
    private String namespace;
    @ApiModelProperty(value = "内容")
    private String content;
}
