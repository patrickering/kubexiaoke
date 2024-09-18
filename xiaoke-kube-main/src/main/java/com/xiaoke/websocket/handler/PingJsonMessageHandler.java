package com.xiaoke.websocket.handler;


import com.xiaoke.websocket.config.WebSocketMessageSender;
import com.xiaoke.websocket.message.JsonWebSocketMessage;
import com.xiaoke.websocket.message.PingJsonWebSocketMessage;
import com.xiaoke.websocket.message.PongJsonWebSocketMessage;
import com.xiaoke.websocket.message.WebSocketMessageTypeEnum;
import org.springframework.web.socket.WebSocketSession;

/**
 * 心跳处理，接收到客户端的ping时，立刻回复一个pong
 *
 * @author Hccake 2021/1/4
 * @version 1.0
 */
public class PingJsonMessageHandler implements JsonMessageHandler<PingJsonWebSocketMessage> {

    @Override
    public void handle(WebSocketSession session, PingJsonWebSocketMessage message) {
        JsonWebSocketMessage pongJsonWebSocketMessage = new PongJsonWebSocketMessage();
        WebSocketMessageSender.send(session, pongJsonWebSocketMessage);
    }

    @Override
    public String type() {
        return WebSocketMessageTypeEnum.PING.getValue();
    }

    @Override
    public Class<PingJsonWebSocketMessage> getMessageClass() {
        return PingJsonWebSocketMessage.class;
    }

}
