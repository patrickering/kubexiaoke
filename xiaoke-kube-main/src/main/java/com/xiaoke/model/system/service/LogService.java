

package com.xiaoke.model.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xiaoke.entity.system.entity.SysLog;
import com.xiaoke.entity.system.vo.PreLogVo;

import java.util.List;

/**
 * <p>
 * 日志表 服务类
 * </p>
 *
 * @author xiaoke
 * @since 2017-11-20
 */
public interface LogService extends IService<SysLog> {


    /**
     * 批量插入前端错误日志
     *
     * @param preLogVoList 日志信息
     * @return true/false
     */
    Boolean saveBatchLogs(List<PreLogVo> preLogVoList);
}
