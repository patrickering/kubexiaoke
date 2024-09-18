package com.xiaoke.common.core.utils.cron;

import cn.hutool.core.util.StrUtil;
import org.springframework.scheduling.support.CronSequenceGenerator;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * 定时表达式工具类
 *
 * @author xiaoke
 */
public class CronUtils {
    /**
     * 按时间计算下次执行时间
     *
     * @param cron
     * @return
     * @Title: getExcuteTime
     * @data:2019年8月19日上午11:34:13
     * @author:zhangsy
     */
    public static Date getExcuteTime(String cron, Date date) {
        if (StrUtil.isEmpty(cron)) {
            throw new IllegalArgumentException("cron表达式不可为空");
        }
        CronSequenceGenerator cronSequenceGenerator = new CronSequenceGenerator(cron);
        return cronSequenceGenerator.next(date);
    }

    /**
     * 根据当前计算下次执行时间
     *
     * @param cron
     * @return
     * @Title: getExcuteTimeByNow
     * @data:2019年8月19日上午11:34:13
     * @author:zhangsy
     */
    public static Date getExcuteTimeByNow(String cron) {
        return getExcuteTime(cron, new Date());
    }

    /**
     * 计算多个执行时间
     *
     * @param cron  表达式
     * @param count 执行时间个数
     * @return
     * @Title: getExcuteTimes
     * @data:2019年8月19日上午11:25:21
     * @author:zhangsy
     */
    public static List<Date> getExcuteTimes(String cron, Integer count) {
        if (StrUtil.isEmpty(cron)) {
            throw new IllegalArgumentException("cron表达式不可为空");
        }
        count = count == null || count < 1 ? 1 : count;
        CronSequenceGenerator cronSequenceGenerator = new CronSequenceGenerator(cron);
        List<Date> list = new ArrayList<Date>(count);
        Date nextTimePoint = new Date();
        for (int i = 0; i < count; i++) {
            // 计算下次时间点的开始时间
            nextTimePoint = cronSequenceGenerator.next(nextTimePoint);
            list.add(nextTimePoint);
        }
        return list;
    }


    //"ss mm HH dd MM ? yyyy"
    private static final SimpleDateFormat sdf = new SimpleDateFormat("ss mm HH dd MM ?");

    /***
     *  功能描述：日期转换cron表达式
     * @param date
     * @return
     */
    public static String formatDateByPattern(Date date) {
        String formatTimeStr = null;
        if (Objects.nonNull(date)) {
            formatTimeStr = sdf.format(date);
        }
        return formatTimeStr;
    }

    /***
     * 获取Cro表达式
     * date 执行时间
     * @return
     */
    public static String getCron(Date date) {
        return formatDateByPattern(date);
    }

    public static void main(String[] args) {
        Date date = new Date("2020/02/24 00:00:00");
        String cron = CronUtils.getCron(date);
        System.out.println(cron);
    }
}
