

package com.xiaoke.annotation.field.handler;

import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.xiaoke.entity.field.ServiceFieldValue;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;

/**
 * @author xiaoke
 * @date 2021年5月24日17:02:12
 * <p>
 * 自定义字段值处理器
 */
@Slf4j
@Component
@AllArgsConstructor
public class ServiceFieldValueHandler {
    public Object value(ServiceFieldValue fieldValue, Field field, Object instance) {
        //条件
        Object obj = ReflectUtil.invoke(instance, "get" + StrUtil.upperFirst(fieldValue.fieldBy()));
        //获取实例bean
        Object bean = SpringUtil.getBean(fieldValue.service());
        Object invoke = ReflectUtil.invoke(bean, fieldValue.method(), obj);
        return invoke;
    }
}
