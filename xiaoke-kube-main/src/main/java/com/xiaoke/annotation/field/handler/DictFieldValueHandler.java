

package com.xiaoke.annotation.field.handler;

import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import com.xiaoke.entity.field.DictFieldValue;
import com.xiaoke.entity.system.entity.DictItem;
import com.xiaoke.model.system.service.DictItemService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author xiaoke
 * @date 2021年5月24日17:02:12
 * <p>
 * 字典字段值处理器
 */
@Slf4j
@Component
@AllArgsConstructor
public class DictFieldValueHandler {
    private final DictItemService dictItemService;

    public Object value(DictFieldValue fieldValue, Object instance) {
        Object obj = ReflectUtil.invoke(instance, "get" + StrUtil.upperFirst(fieldValue.fieldBy()));
        if (obj == null || StrUtil.isEmpty(obj.toString())) {
            return null;
        }
        List<DictItem> sysDict = dictItemService.getDictByType(fieldValue.dictType());
        if ("".equals(fieldValue.getColumn())) {
            return sysDict.stream().filter(item -> item.getValue().equals(obj.toString())).map(DictItem::getLabel).distinct().findFirst().orElse(null);
        }
        if ("remarks".equals(fieldValue.getColumn())) {
            return sysDict.stream().filter(item -> item.getValue().equals(obj.toString())).map(DictItem::getRemarks).distinct().findFirst().orElse(null);
        }
        if ("description".equals(fieldValue.getColumn())) {
            return sysDict.stream().filter(item -> item.getValue().equals(obj.toString())).map(DictItem::getDescription).distinct().findFirst().orElse(null);
        }
        return null;
    }
}
