package com.xiaoke.entity.kube.entity;

import lombok.Data;

@Data
public class PodLog {
    private String name;
    private String container;
}
