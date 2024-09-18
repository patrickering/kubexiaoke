package com.xiaoke.entity.kube.entity;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 实体类
 *
 * @author chendengwen
 * @date 2019-11-09 23:32:19
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class Monitor extends Model<Monitor> {
    /**
     * 命名空间
     */
    private String namespace;

    /**
     * 容器
     */
    private String container;

}