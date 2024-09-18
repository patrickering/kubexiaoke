package com.xiaoke.entity.kube.entity;


import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;

/**
*  应用 实体类
*
*  @author xiaoke
*  @date 2024-08-03 15:34:01
*/
@Data
@ApiModel(value = "应用")
@EqualsAndHashCode(callSuper = true)
@TableName("kube_app")
public class App extends Model<App> {


    /**
    * 主键ID
    */
    @ApiModelProperty(value = "主键ID")
    @TableId
    private Integer id;
    /**
    * 命名空间
    */
    @ApiModelProperty(value = "命名空间")
    private String namespace;

    /**
    * 名称
    */
    @ApiModelProperty(value = "名称")
    private String name;
    /**
    * 应用类型
    */
    @ApiModelProperty(value = "应用类型")
    private String type;
    /**
    * 状态
     * 10=创建中
     * 20=运行中
     * 30=停止中
     * 40=已停止
     * 50=重启中
     * 60=异常
     * 70=更新中
    */
    @ApiModelProperty(value = "状态")
    private String state;
    /**
    * 标识
    */
    @ApiModelProperty(value = "标识")
    private String sign;
    /**
    * 部署数量
    */
    @ApiModelProperty(value = "部署数量")
    private Integer count;
    /**
    * 当前运行数量
    */
    @ApiModelProperty(value = "当前运行数量")
    private Integer nowCount;
    /**
    * 镜像
    */
    @ApiModelProperty(value = "镜像")
    private String image;
    /**
    * 镜像版本
    */
    @ApiModelProperty(value = "镜像版本")
    private String imageVersion;
    /**
    * cpu请求
    */
    @ApiModelProperty(value = "cpu请求")
    private Double cpuRequest;
    /**
    * cpu上限
    */
    @ApiModelProperty(value = "cpu上限")
    private Double cpuLimit;
    /**
    * 内存请求
    */
    @ApiModelProperty(value = "内存请求")
    private Integer memoryRequest;
    /**
    * 内存请求单位
    */
    @ApiModelProperty(value = "内存请求单位")
    private String memoryRequestCompany;
    /**
    * 内存上限
    */
    @ApiModelProperty(value = "内存上限")
    private Integer memoryLimit;
    /**
    * 内存上限单位
    */
    @ApiModelProperty(value = "内存上限单位")
    private String memoryLimitCompany;
    /**
    * 是否打开环境变量配置文件
    */
    @ApiModelProperty(value = "是否打开环境变量配置文件")
    private Boolean openEnvConfig;
    /**
    * 是否打开自定义环境变量
    */
    @ApiModelProperty(value = "是否打开自定义环境变量")
    private Boolean openCustomEnv;
    /**
    * 是否添加配置文件
    */
    @ApiModelProperty(value = "是否添加配置文件")
    private Boolean openConfig;
    /**
    * 更新策略类型
    */
    @ApiModelProperty(value = "更新策略类型")
    private String updateType;
    /**
    * 最大不可用
    */
    @ApiModelProperty(value = "最大不可用")
    private Integer unavailable;
    /**
    * 最大超量
    */
    @ApiModelProperty(value = "最大超量")
    private Integer excess;
    /**
    * DNS 策略类型
    */
    @ApiModelProperty(value = "DNS 策略类型")
    private String dnsType;
    /**
    * 是否启用健康检查
    */
    @ApiModelProperty(value = "是否启用健康检查")
    private Boolean openHealth;
    /**
    * 健康检查协议
    */
    @ApiModelProperty(value = "健康检查协议")
    private String healthScheme;
    /**
    * 健康检查端口
    */
    @ApiModelProperty(value = "健康检查端口")
    private Integer healthPort;
    /**
    * 健康检查路径
    */
    @ApiModelProperty(value = "健康检查路径")
    private String healthPath;
    /**
    * 健康检查失败次数
    */
    @ApiModelProperty(value = "健康检查失败次数")
    private Integer failureThreshold;
    /**
    * 首次检查时间
    */
    @ApiModelProperty(value = "首次检查时间")
    private Integer initialDelay;
    /**
    * 探测周期
    */
    @ApiModelProperty(value = "探测周期")
    private Integer periodSeconds;
    /**
    * 成功阈值
    */
    @ApiModelProperty(value = "成功阈值")
    private Integer successThreshold;
    /**
    * 超时时间
    */
    @ApiModelProperty(value = "超时时间")
    private Integer timeoutSeconds;
    /**
    * 描述
    */
    @ApiModelProperty(value = "描述")
    private String remark;
    /**
    * 是否删除(0=false，1=true)
    */
    @ApiModelProperty(value = "是否删除(0=false，1=true)")
    @TableLogic
    private String delFlag;
    /**
    * 创建人
    */
    @ApiModelProperty(value = "创建人")
    private Integer createBy;
    /**
    * 创建时间
    */
    @ApiModelProperty(value = "创建时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
    /**
    * 修改人
    */
    @ApiModelProperty(value = "修改人")
    private Integer updateBy;
    /**
    * 修改时间
    */
    @ApiModelProperty(value = "修改时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;
}