package com.xiaoke.entity.kube.dto;

import com.baomidou.mybatisplus.annotation.TableName;
import com.xiaoke.entity.kube.entity.AppPodRecord;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 应用pod记录 实体类
 *
 * @author chendengwen
 * @date 2019-12-23 23:40:14
 */
@Data
@TableName("kube_app_pod_record")
@EqualsAndHashCode(callSuper = true)
public class AppPodRecordDTO extends AppPodRecord {
    /**
     * 开始时间
     */
    @ApiModelProperty(value = "开始时间")
    private String startTime;
    /**
     * 结束时间
     */
    @ApiModelProperty(value = "结束时间")
    private String endTime;
}