package com.xiaoke.model.system.controller;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import com.wf.captcha.ArithmeticCaptcha;
import com.xiaoke.annotation.idempotent.Idempotent;
import com.xiaoke.annotation.token.Token;
import com.xiaoke.annotation.validate.ValidateFiled;
import com.xiaoke.annotation.validate.ValidateGroup;
import com.xiaoke.common.core.constant.CacheConstants;
import com.xiaoke.common.core.enums.LoginTypeEnum;
import com.xiaoke.common.core.utils.JsonUtils;
import com.xiaoke.common.core.utils.R;
import com.xiaoke.common.core.utils.StringUtils;
import com.xiaoke.entity.system.entity.Login;
import com.xiaoke.entity.system.entity.LoginLog;
import com.xiaoke.entity.system.entity.SysUser;
import com.xiaoke.entity.system.entity.User;
import com.xiaoke.entity.system.vo.UserVO;
import com.xiaoke.model.system.handler.LoginHandler;
import com.xiaoke.model.system.service.LoginLogService;
import com.xiaoke.model.system.service.MenuService;
import com.xiaoke.model.system.service.RoleService;
import com.xiaoke.model.system.service.UserService;
import com.xiaoke.utils.JoinUtil;
import com.xiaoke.utils.SecurityUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Api(value = "登录相关API", tags = {"登录接口"})
@RestController
@Slf4j
@AllArgsConstructor
@RequestMapping("${controller.prefix}/${controller.system.prefix}/login")
public class LoginController {
    private final Map<String, LoginHandler> loginHandlerMap;

    private static final Integer DEFAULT_IMAGE_WIDTH = 100;
    private static final Integer DEFAULT_IMAGE_HEIGHT = 40;
    private final RedisTemplate redisTemplate;
    private final UserService userService;
    private final RoleService roleService;
    private final MenuService menuService;
    private final LoginLogService loginLogService;

    @ApiOperation("用户名密码登录图形验证码")
    @GetMapping("/code/{randomStr}")
    @Token(value = false)
    public void registerImage(@PathVariable String randomStr, HttpServletResponse response) throws IOException {
        ArithmeticCaptcha captcha = null;
        String result = null;
        do {
            captcha = new ArithmeticCaptcha(DEFAULT_IMAGE_WIDTH, DEFAULT_IMAGE_HEIGHT);
            if (captcha == null) {
                log.error("========> 生成图形验证码失败");
                return;
            }
            result = captcha.text();
        } while (Integer.valueOf(result) <= 0);
        //保存验证码信息
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.opsForValue().set(CacheConstants.DEFAULT_CODE_KEY + randomStr, result
                , CacheConstants.CODE_TIME, TimeUnit.MINUTES);
        // 转换流信息写出
        response.setContentType("image/jpeg");
        captcha.out(response.getOutputStream());
    }

    /**
     * 登录
     *
     * @param type    帐号密码=USERNAME, 手机号=SMS,微信公众号=WX,微信小程序=WXMINI
     * @param login   帐号密码： username  password captcha  randomStr
     *                手机号： phone  code
     *                微信公众号-微信小程序：code
     * @param request
     * @return
     */
    @ApiOperation("登录")
    @GetMapping(value = "login/{type}")
    @ValidateGroup(fileds = {
            @ValidateFiled(index = 0, notNull = true, msg = "缺少登录类型参数")
    })
    @Token(value = false)
    public R<String> login(@PathVariable String type, Login login, HttpServletRequest request) {
        if (StrUtil.isEmpty(type)) {
            return R.failed("缺少登录类型参数【type】");
        }
        LoginHandler loginHandler = loginHandlerMap.get(type);
        if (loginHandler == null) {
            return R.failed("不支持登录类型【" + type + "】");
        }
        R<User> userR = loginHandlerMap.get(type).handle(login);
        if (!userR.getState()) {
            return R.failed(userR.getMsg());
        }
        return R.ok(loginHandle(login, userR.getData(), request));
    }

    /**
     * 登录统一处理
     *
     * @param user
     * @param request
     * @return
     */
    private String loginHandle(Login login, User user, HttpServletRequest request) {
        SysUser sysUser = Convert.convert(SysUser.class, user);
        sysUser.setPassword(null);
        if (user.getId() != null) {
            UserVO userVO = JoinUtil.entity(user, UserVO.class);
            sysUser.setRole(userVO.getRole());
            //赋值角色
            //设置权限列表（menu.permission）
            Set<String> permissions = new HashSet<>();
            if (sysUser.getRoleId() != null) {
                List<Integer> idList = new ArrayList<>();
                idList.add(sysUser.getRoleId());
                List<String> permissionList = menuService.findPermissionByRoleIdList(idList);
                permissions.addAll(permissionList);
                sysUser.setPermissions(ArrayUtil.toArray(permissions, String.class));
            }
            //更新用户登录ip登录日期
            String loginIp = StringUtils.getRemoteAddr(request);
            sysUser.setLoginDate(DateUtil.toLocalDateTime(new Date()));

            sysUser.setLoginIp(loginIp);
            userService.updateById(sysUser);
            //设置token
            String token = SecurityUtils.setUser(sysUser);
            //添加登录日志
            LoginLog loginLog = new LoginLog();
            loginLog.setType(LoginTypeEnum.LOGIN.getType());
            loginLog.setIp(loginIp);
            loginLog.setSource(login.getSource());
            loginLog.setToken(token);
            loginLog.setParam(JsonUtils.objectToJson(login));
            loginLog.setCreateBy(user.getId());
            loginLogService.save(loginLog);
            return token;
        } else {
            //设置token
            String token = SecurityUtils.setUser(sysUser);
            return token;
        }
    }


    @ApiOperation("获取用户信息")
    @GetMapping(value = "/info")
    public R<SysUser> info() {
        SysUser session = SecurityUtils.getUser();
        if (session != null) {
            session.setPassword(null);
        }
        return R.ok(session);
    }


    @ApiOperation("退出登录")
    @Idempotent(value = false)
    @DeleteMapping("out-login")
    @Token(value = false)
    public R outLogin(HttpServletRequest request) {
        SysUser user = SecurityUtils.getUser();
        if (user != null) {
            String loginIp = StringUtils.getRemoteAddr(request);
            String token = request.getHeader("token");

            //添加登录日志
            LoginLog loginLog = new LoginLog();
            loginLog.setType(LoginTypeEnum.OUT.getType());
            loginLog.setIp(loginIp);
            loginLog.setToken(token);
            loginLog.setCreateBy(user.getId());
            loginLogService.save(loginLog);
        }
        SecurityUtils.removeUser();
        return R.ok().setMsg("已安全退出！");
    }
}
