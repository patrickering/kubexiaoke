package com.xiaoke.entity.kube.dto;


import com.baomidou.mybatisplus.annotation.TableName;
import com.xiaoke.entity.kube.entity.Project;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * 项目 DTO
 *
 * @author xiaoke
 * @date 2024-08-03 14:21:50
 */
@Data
@ApiModel(value = "项目")
@EqualsAndHashCode(callSuper = true)
@TableName("kube_project")
public class ProjectDTO extends Project {


}