

package com.xiaoke.model.kube.handler;


import com.xiaoke.entity.kube.entity.App;
import com.xiaoke.entity.kube.entity.Push;
import com.xiaoke.entity.system.entity.User;

/**
 * @author xiaoke
 * @date 2018/11/18
 * <p>
 * 推送处理器
 */
public interface PushHandler {
    /**
     * 发送
     *
     * @return
     */
    Boolean send(Integer contentType, Push push, User user, String namespace, App app);
}
