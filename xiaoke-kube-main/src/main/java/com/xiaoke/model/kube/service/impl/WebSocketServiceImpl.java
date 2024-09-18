package com.xiaoke.model.kube.service.impl;

import cn.hutool.core.util.StrUtil;
import com.xiaoke.entity.kube.entity.SockMessage;
import com.xiaoke.model.kube.service.WebSocketService;
import com.xiaoke.websocket.distribute.MessageDO;
import com.xiaoke.websocket.distribute.RedisMessageDistributor;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * webSocket 实现
 */
@Service
@AllArgsConstructor
@Slf4j
public class WebSocketServiceImpl implements WebSocketService {
    private final RedisMessageDistributor redisMessageDistributor;

    /**
     * 发送消息
     *
     * @param message
     */
    @Override
    public void send(SockMessage message) {
        log.info("发送websocket消息：Namespace:{},message:{}", message.getNamespace(), message);

        String target = "";
        if (StrUtil.isNotEmpty(message.getNamespace())) {
            target = message.getNamespace();
        }
        List<Object> remindUserList = new ArrayList<>();
        remindUserList.add(target);
        // websocket 发送消息
        MessageDO messageDO = new MessageDO();
        messageDO.setNeedBroadcast(Boolean.FALSE);
        messageDO.setSessionKeys(remindUserList);
        messageDO.setMessageText(message.getContent());
        redisMessageDistributor.distribute(messageDO);
    }
}
