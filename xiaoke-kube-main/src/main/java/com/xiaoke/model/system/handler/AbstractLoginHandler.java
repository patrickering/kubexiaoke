

package com.xiaoke.model.system.handler;

import com.xiaoke.common.core.utils.R;
import com.xiaoke.entity.system.entity.Login;
import com.xiaoke.entity.system.entity.User;

/**
 * @author xiaoke
 * @date 2018/11/18
 */
public abstract class AbstractLoginHandler implements LoginHandler {

    /**
     * 处理方法
     *
     * @param login 登录参数
     * @return
     */
    @Override
    public R<User> handle(Login login) {
        R<User> check = check(login);
        if (!check.getState()) {
            return check;
        }
        String identify = identify(login);
        User info = info(identify);
        return afterHandle(info, login);
    }
}
