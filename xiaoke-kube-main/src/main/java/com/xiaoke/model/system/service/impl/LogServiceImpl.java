

package com.xiaoke.model.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xiaoke.common.core.constant.Constant;
import com.xiaoke.entity.system.entity.SysLog;
import com.xiaoke.entity.system.vo.PreLogVo;
import com.xiaoke.model.system.mapper.LogMapper;
import com.xiaoke.model.system.service.LogService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 日志表 服务实现类
 * </p>
 *
 * @author xiaoke
 * @since 2017-11-20
 */
@Service
public class LogServiceImpl extends ServiceImpl<LogMapper, SysLog> implements LogService {

    /**
     * 批量插入前端错误日志
     *
     * @param preLogVoList 日志信息
     * @return true/false
     */
    @Override
    public Boolean saveBatchLogs(List<PreLogVo> preLogVoList) {
        List<SysLog> sysLogs = preLogVoList.stream()
                .map(pre -> {
                    SysLog log = new SysLog();
                    log.setType(Constant.STATUS_LOCK);
                    log.setTitle(pre.getInfo());
                    log.setException(pre.getStack());
                    log.setParams(pre.getMessage());
                    log.setCreateTime(LocalDateTime.now());
                    log.setRequestUri(pre.getUrl());
                    log.setCreateBy(pre.getUser());
                    return log;
                })
                .collect(Collectors.toList());
        return this.saveBatch(sysLogs);
    }
}
