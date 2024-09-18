

package com.xiaoke.model.kube.handler;


import com.xiaoke.entity.kube.entity.App;
import com.xiaoke.entity.kube.entity.Push;
import com.xiaoke.entity.system.entity.User;

/**
 * @author cdw
 * @date 2018/11/18
 */
public abstract class AbstractPushHandler implements PushHandler {

    @Override
    public Boolean send(Integer contentType, Push push, User user, String namespace, App app) {
        return true;
    }
}
