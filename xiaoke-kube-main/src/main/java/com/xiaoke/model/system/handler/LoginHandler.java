

package com.xiaoke.model.system.handler;

import com.xiaoke.common.core.utils.R;
import com.xiaoke.entity.system.entity.Login;
import com.xiaoke.entity.system.entity.User;

/**
 * @author xiaoke
 * @date 2018/11/18
 * <p>
 * 登录处理器
 */
public interface LoginHandler {

    /***
     * 数据合法性校验
     * @param login 通过用户传入获取唯一标识
     * @return
     */
    R<User> check(Login login);

    /**
     * 通过用户传入获取唯一标识
     *
     * @param login
     * @return
     */
    String identify(Login login);

    /**
     * 获取用户信息
     *
     * @param identify
     * @return
     */
    User info(String identify);

    /**
     * 处理方法
     *
     * @param login 登录参数
     * @return
     */
    R<User> handle(Login login);

    /**
     * 获取用户信息后处理
     *
     * @param user
     * @param login
     * @return
     */
    R<User> afterHandle(User user, Login login);
}
