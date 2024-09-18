package com.xiaoke.websocket.distribute;

import cn.hutool.json.JSONUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 消息分发器
 *
 * @author Hccake 2021/1/12
 * @version 1.0
 */
@RequiredArgsConstructor
public class RedisMessageDistributor implements MessageDistributor {

	private final StringRedisTemplate stringRedisTemplate;

	/**
	 * 消息分发
	 * @param messageDO 发送的消息
	 */
	@Override
	public void distribute(MessageDO messageDO) {
		// 包装 sessionKey 适配分布式多环境
		List<Object> sessionKeyList = messageDO.getSessionKeys().stream()
				.map(key -> String.format("%s", key))
				.collect(Collectors.toList());
		messageDO.setSessionKeys(sessionKeyList);

		String str = JSONUtil.toJsonStr(messageDO);
		stringRedisTemplate.convertAndSend(RedisWebsocketMessageListener.CHANNEL, str);
	}

}
