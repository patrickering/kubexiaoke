package com.xiaoke.common.core.constant;

/**
 * 系统常量
 *
 * @author xiaoke
 */
public interface Constant {
    /**
     * header中租户ID
     */
    String TENANT_ID = "TENANT-ID";

    /**
     * 开发环境
     */
    String DEV = "dev";
    /**
     * 生产环境
     */
    String PROD = "prod";

    /**
     * 正常
     */
    String STATUS_NORMAL = "0";

    /**
     * 锁定
     */
    String STATUS_LOCK = "9";

    /**
     * 成功标记
     */
    Integer SUCCESS = 0;

    /**
     * 失败标记
     */
    Integer FAIL = 1;

    /**
     * 菜单树根节点
     */
    Integer MENU_TREE_ROOT_ID = -1;

    /**
     * 自动注册
     */
    Boolean AUTO_REGISTER = Boolean.FALSE;

    /**
     * 默认角色id
     */
    String DEFAULT_ROLE_CODE = "ROLE_ADMIN";

    /**
     * 密码加密盐
     */
    String PASSWORD_SALT = "xiaokekube123456";

    /**
     * 请求头命名空间key
     */
    String HEADER_NAME_SPACE_KEY = "namespace";
}
