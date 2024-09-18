package com.xiaoke.utils;

import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.diboot.core.binding.Binder;
import com.diboot.core.binding.QueryBuilder;
import com.diboot.core.binding.RelationsBinder;
import com.diboot.core.binding.query.dynamic.ExtQueryWrapper;
import com.diboot.core.exception.BusinessException;
import com.diboot.core.vo.Pagination;
import com.xiaoke.annotation.field.handler.AggregateFieldValueHandler;
import com.xiaoke.annotation.field.handler.DictFieldValueHandler;
import com.xiaoke.annotation.field.handler.ServiceFieldValueHandler;
import com.xiaoke.annotation.field.handler.UserFieldValueHandler;
import com.xiaoke.entity.field.AggregateFieldValue;
import com.xiaoke.entity.field.DictFieldValue;
import com.xiaoke.entity.field.ServiceFieldValue;
import com.xiaoke.entity.field.UserFieldValue;
import lombok.AllArgsConstructor;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author xiaoke
 * @date 2021年5月24日17:02:12
 * <p>
 * Diboot
 */
@AllArgsConstructor
public class JoinUtil extends Binder {

    /**
     * 单表集合转关联查询
     *
     * @param entityList
     * @param voClass
     * @param <E>
     * @param <VO>
     * @return
     */
    public static <E, VO> List<VO> list(List<E> entityList, Class<VO> voClass, Boolean... bool) {
        List<VO> vos = RelationsBinder.convertAndBind(entityList, voClass);
        Boolean flag = bool == null || bool.length <= 0 || (bool[0] != null && bool[0]);
        if (flag) {
            for (Object o : vos) {
                o = setFieldValue(o);
            }
        }
        return vos;
    }

    /**
     * 单表实体查询转关联查询
     *
     * @param entity
     * @param voClass
     * @param <E>
     * @param <VO>
     * @return
     */
    public static <E, VO> VO entity(E entity, Class<VO> voClass, Boolean... bool) {
        if (entity == null) {
            return null;
        }
        Boolean flag = bool == null || bool.length <= 0 || (bool[0] != null && bool[0]);
        if (flag) {
            return (VO) setFieldValue(RelationsBinder.convertAndBind(entity, voClass));
        } else {
            return RelationsBinder.convertAndBind(entity, voClass);
        }
    }

    /**
     * 单表分页查询转关联查询
     *
     * @param page
     * @param voClass
     * @param <VO>
     * @return
     */
    public static <VO> Page page(Page page, Class<VO> voClass, Boolean... bool) {
        List records = page.getRecords();
        List list = RelationsBinder.convertAndBind(records, voClass);
        Boolean flag = bool == null || bool.length <= 0 || (bool[0] != null && bool[0]);
        if (flag) {
            for (Object o : list) {
                o = setFieldValue(o);
            }
        }
        page.setRecords(list);
        return page;
    }

    /**
     * 多表分页
     *
     * @param page
     * @param entity
     * @param voClass
     * @param <E>
     * @param <VO>
     * @return
     */
    public static <E, VO> Page page(Page page, E entity, Class<VO> voClass) {
        if (page == null) {
            page = new Page();
            page.setCurrent(1);
            page.setSize(10);
        }
        ExtQueryWrapper extQueryWrapper = QueryBuilder.toDynamicJoinQueryWrapper(entity);
        Pagination pagination = new Pagination();
        pagination.setPageIndex((int) page.getCurrent());
        pagination.setPageSize((int) page.getSize());
        try {
            List<E> list = extQueryWrapper.queryList(voClass, pagination);
            page.setRecords(JoinUtil.list(list, voClass));
        } catch (BusinessException e) {

        }
        page.setTotal(pagination.getTotalCount());
        return page;
    }


    /**
     * 筛选FieldValue
     *
     * @param e
     * @return
     */
    private static Boolean filterFieldValue(Field e) {
        return e.isAnnotationPresent(UserFieldValue.class)
                || e.isAnnotationPresent(DictFieldValue.class)
                || e.isAnnotationPresent(ServiceFieldValue.class)
                || e.isAnnotationPresent(AggregateFieldValue.class);
    }

    /**
     * 字段赋值
     *
     * @param instance
     * @return
     */
    private static Object setFieldValue(Object instance) {
        Class<?> clazz = instance.getClass();
        List<Field> fields = Arrays.stream(clazz.getDeclaredFields()).filter(JoinUtil::filterFieldValue).collect(Collectors.toList());
        for (Field field : fields) {
            Object value = null;
            try {
                if (field.isAnnotationPresent(UserFieldValue.class)) {
                    //用户字段赋值注解实现
                    UserFieldValueHandler userFieldValueHandler = SpringUtil.getBean("userFieldValueHandler", UserFieldValueHandler.class);
                    value = userFieldValueHandler.value(field.getAnnotation(UserFieldValue.class), instance);
                } else if (field.isAnnotationPresent(DictFieldValue.class)) {
                    //字典字段赋值注解实现
                    DictFieldValueHandler dictFieldValueHandler = SpringUtil.getBean("dictFieldValueHandler", DictFieldValueHandler.class);
                    value = dictFieldValueHandler.value(field.getAnnotation(DictFieldValue.class), instance);
                } else if (field.isAnnotationPresent(ServiceFieldValue.class)) {
                    //自定义Service字段赋值注解实现
                    ServiceFieldValueHandler serviceFieldValueHandler = SpringUtil.getBean("serviceFieldValueHandler", ServiceFieldValueHandler.class);
                    value = serviceFieldValueHandler.value(field.getAnnotation(ServiceFieldValue.class), field, instance);
                } else if (field.isAnnotationPresent(AggregateFieldValue.class)) {
                    //聚合查询字段赋值注解实现
                    AggregateFieldValueHandler aggregateFieldValueHandler = SpringUtil.getBean("aggregateFieldValueHandler", AggregateFieldValueHandler.class);
                    value = aggregateFieldValueHandler.value(field.getAnnotation(AggregateFieldValue.class), instance);
                }
                if (value != null) {
                    //获取值注入entity
                    if (field.getType() == BigDecimal.class) {
                        value = new BigDecimal(String.valueOf(value));
                    }
                    //获取值注入entity
                    ReflectUtil.invoke(instance, "set" + StrUtil.upperFirst(field.getName()), value);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        return instance;
    }
}
