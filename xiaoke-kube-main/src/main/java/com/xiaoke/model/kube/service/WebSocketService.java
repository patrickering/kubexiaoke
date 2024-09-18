package com.xiaoke.model.kube.service;


import com.xiaoke.entity.kube.entity.SockMessage;

/**
 * webSocket Service
 */
public interface WebSocketService {
    /**
     * 发送消息
     *
     * @param message
     */
    void send(SockMessage message);
}
