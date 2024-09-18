package com.xiaoke.entity.kube.entity;

import lombok.Data;

/**
 * jenkins 实体
 */
@Data
public class Jenkins {
    public Jenkins(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }

    public Jenkins() {
    }

    private String url;
    private String username;
    private String password;
}
