package com.xiaoke.exception.event;

import com.xiaoke.entity.system.entity.Error;
import com.xiaoke.model.system.service.ErrorService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.Async;

/**
 * @author xiaoke
 * 异步监听异常事件
 */
@Slf4j
@AllArgsConstructor
public class SysErrorListener {
    private final ErrorService errorService;

    @Async
    @Order(1)
    @EventListener(SysErrorEvent.class)
    public void saveSysError(SysErrorEvent event) {
        Error error = event.getError();
        errorService.save(error);
    }
}
