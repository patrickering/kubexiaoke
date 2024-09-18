package com.xiaoke.common.core.utils;

import cn.hutool.core.lang.Validator;
import cn.hutool.core.util.IdcardUtil;
import cn.hutool.core.util.ReUtil;


/**
 * 数据校验工具类
 * 继承hutool 有特殊需求自己新增
 * 文档地址 https://www.hutool.cn/docs/#/core/语言特性/字段验证器-Validator
 * API地址 https://apidoc.gitee.com/loolly/hutool/ 寻找cn.hutool.core.lang.Validator
 *
 * @author zzf
 * @date 2020/5/14
 */
public class VerificationUtils extends Validator {

    /**
     * 验证是否英文字母 、数字和下划线
     *
     * @param value 被验证值
     * @return
     */
    public static boolean isGeneral(CharSequence value) {
        return Validator.isGeneral(value);
    }

    /**
     * 判断是否是数字
     *
     * @param value 字符串结构CharSequence
     * @return
     */
    public static boolean isNumber(CharSequence value) {
        return Validator.isNumber(value);
    }

    /**
     * 是否手机号
     * 正则匹配 匹配规则略宽
     *
     * @param value 手机号
     * @return
     */
    public static boolean isPhone(CharSequence value) {
        return isMobile(value);
    }

    /**
     * 是否为邮箱
     *
     * @param value 邮箱
     * @return
     */
    public static boolean isEmail(CharSequence value) {
        return Validator.isEmail(value);
    }

    /**
     * 验证是否为身份证号
     *
     * @param value 身份证号
     * @return
     */
    public static boolean isCard(CharSequence value) {
        return IdcardUtil.isValidCard(String.valueOf(value));
    }

    /**
     * 判断是否匹配正则
     *
     * @param pattern 正则表达式
     * @param value   要判断的数据
     * @return
     */
    public static boolean isMatchRegex(String pattern, CharSequence value) {
        return ReUtil.isMatch(pattern, value);
    }

}
