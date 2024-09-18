package com.xiaoke.annotation.log.event;

import com.xiaoke.entity.system.entity.SysLog;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author xiaoke
 * 系统日志事件
 */
@Getter
@AllArgsConstructor
public class SysLogEvent {
    private final SysLog sysLog;
}
