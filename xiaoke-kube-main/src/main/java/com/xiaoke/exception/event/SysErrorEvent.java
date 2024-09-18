package com.xiaoke.exception.event;

import com.xiaoke.entity.system.entity.Error;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author xiaoke
 * 系统异常事件
 */
@Getter
@AllArgsConstructor
public class SysErrorEvent {
    private final Error error;
}
