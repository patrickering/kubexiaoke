package com.xiaoke.common.core.constant;

/**
 * @author xiaoke
 * @date 2019-04-28
 * <p>
 * 缓存的key 常量
 */
public interface CacheConstants {
    String base = "xiaoke_kube_";
    String baseMax = "XIAOKE_KUBE_";

    /**
     * 验证码前缀
     */
    String DEFAULT_CODE_KEY = baseMax + "DEFAULT_CODE_KEY:";

    /**
     * 验证码保存时间 10 分钟
     */
    Integer CODE_TIME = 10;

    /**
     * 用户信息缓存
     */
    String USER_DETAILS = base + "user_details:";

    /**
     * 用户缓存时间 2小时
     */
    Integer USER_DETAILS_TIME = 2;

    /**
     * 字典信息缓存
     */
    String DICT_DETAILS = base + "dict_details";

    /**
     * 参数缓存
     */
    String PARAMS_DETAILS = base + "params_details";


    /**
     * 数据权限类型列表缓存
     */
    String SCOPE_TYPE_LIST = base + "scope_type_list";

    /**
     * 数据权限缓存
     */
    String SCOPE_DETAILS = base + "scope_details";

    /**
     * 角色数据权限缓存
     */
    String ROLE_SCOPE_DETAILS = base + "role_scope_details";

    /**
     * 数据权限等级缓存
     */
    String SCOPE_LEVEL_DETAILS = base + "scope_level_details";

    /**
     * 幂等前缀
     */
    String IDEMPOTENT_KEY = base + "idempotent_key:";

    /**
     * 手机号图形验证码
     */
    String PHONE_IMAGE_KEY = baseMax + "PHONE_IMAGE_KEY:";

    /**
     * 手机号验证码
     */
    String PHONE_CODE_KEY = baseMax + "PHONE_CODE_KEY:";

    /**
     * 验证码长度
     */
    String CODE_SIZE = "4";

    /**
     * 手机号验证码保存时间 10 分钟
     */
    Integer PHONE_CODE_TIME = 10;

    /**
     * 手机号验证码锁
     */
    String PHONE_CODE_LOCK = baseMax + "PHONE_CODE_LOCK:";

    /**
     * 请求签名key
     */
    String REQUEST_SIGN = baseMax + "REQUEST_SIGN:";

    /**
     * 请求盐
     */
    String REQUEST_SALT = "xiaoke_kube@#";

    /**
     * 登录限制缓存
     */
    String LOGIN_LIMIT = baseMax + "LOGIN_LIMIT:";

}
