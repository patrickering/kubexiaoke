

package com.xiaoke.annotation.field.handler;

import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.IService;
import com.diboot.core.util.ContextHelper;
import com.xiaoke.entity.field.AggregateFieldValue;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author xiaoke
 * @date 2021年5月24日17:02:12
 * <p>
 * 聚合查询字段值处理器
 */
@Slf4j
@Component
@AllArgsConstructor
public class AggregateFieldValueHandler {
    public Object value(AggregateFieldValue fieldValue, Object instance) {
        //条件
        Object obj = null;
        if (StrUtil.isNotEmpty(fieldValue.fieldBy())) {
            obj = ReflectUtil.invoke(instance, "get" + StrUtil.upperFirst(fieldValue.fieldBy()));
        }
        Class entity = fieldValue.entity();
        IService iService = ContextHelper.getIServiceByEntity(entity);
        QueryWrapper<Object> query = Wrappers.query();
        if (StrUtil.isNotEmpty(fieldValue.conditionColumn()) && obj != null) {
            query.eq(fieldValue.conditionColumn(), obj);
        }

        if (StrUtil.isNotEmpty(fieldValue.condition())) {
            String condition = StrUtil.trim(fieldValue.condition());
            String trim = StrUtil.trim(fieldValue.condition());
            if (trim.indexOf("AND") == 0) {
                condition = condition.replace("AND", "");
            }
            if (trim.indexOf("and") == 0) {
                condition = condition.replace("and", "");
            } else if (trim.indexOf("OR") == 0) {
                condition = condition.replace("OR", "");
                query.or();
            } else if (trim.indexOf("or") == 0) {
                condition = condition.replace("or", "");
                query.or();
            }
            query.apply(condition);
        }

        switch (fieldValue.type()) {
            case COUNT:
                return iService.count(query);
            case SUM:
                if (StrUtil.isEmpty(fieldValue.selectColumn())) {
                    return 0;
                }
                query.select("ifnull(sum(" + fieldValue.selectColumn() + "),0) as sumTotal ");
                Map sumMap = iService.getMap(query);
                return Double.valueOf(String.valueOf(sumMap.get("sumTotal")));
            case AVG:
                if (StrUtil.isEmpty(fieldValue.selectColumn())) {
                    return 0;
                }
                query.select("ifnull(avg(" + fieldValue.selectColumn() + "),0) as avgTotal ");
                Map avgMap = iService.getMap(query);
                return Double.valueOf(String.valueOf(avgMap.get("avgTotal")));
            default:
                return 0;
        }
    }

}
