

package com.xiaoke.annotation.field.handler;

import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import com.xiaoke.entity.field.UserFieldValue;
import com.xiaoke.entity.system.entity.User;
import com.xiaoke.model.system.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author xiaoke
 * @date 2021年5月24日17:02:12
 * <p>
 * 用户字段值处理器
 */
@Slf4j
@Component
@AllArgsConstructor
public class UserFieldValueHandler {
    private final UserService userService;

    public Object value(UserFieldValue fieldValue, Object instance) {
        Object obj = ReflectUtil.invoke(instance, "get" + StrUtil.upperFirst(fieldValue.fieldBy()));
        User sysUser = userService.getById((Integer) obj);
        if (sysUser != null && sysUser.getUsername() != null) {
            return sysUser.getName();
        } else {
            return null;
        }
    }
}
