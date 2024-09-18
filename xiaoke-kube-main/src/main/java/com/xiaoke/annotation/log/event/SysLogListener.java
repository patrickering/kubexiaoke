package com.xiaoke.annotation.log.event;

import com.xiaoke.entity.system.entity.SysLog;
import com.xiaoke.model.system.service.LogService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.Async;

/**
 * @author xiaoke
 * 异步监听日志事件
 */
@Slf4j
@AllArgsConstructor
public class SysLogListener {
    private final LogService logService;

    @Async
    @Order
    @EventListener(SysLogEvent.class)
    public void saveSysLog(SysLogEvent event) {
        SysLog sysLog = event.getSysLog();
        logService.save(sysLog);
    }
}
