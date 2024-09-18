package com.xiaoke.model.system.controller;

import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.Mode;
import cn.hutool.crypto.Padding;
import cn.hutool.crypto.symmetric.AES;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xiaoke.annotation.log.SysLog;
import com.xiaoke.annotation.permission.Permission;
import com.xiaoke.annotation.token.Token;
import com.xiaoke.annotation.validate.ValidateFiled;
import com.xiaoke.annotation.validate.ValidateGroup;
import com.xiaoke.common.core.constant.CacheConstants;
import com.xiaoke.common.core.constant.Constant;
import com.xiaoke.common.core.enums.PhoneTypeEnum;
import com.xiaoke.common.core.utils.R;
import com.xiaoke.common.wx.service.WxService;
import com.xiaoke.entity.system.dto.UserDTO;
import com.xiaoke.entity.system.entity.SysUser;
import com.xiaoke.entity.system.entity.User;
import com.xiaoke.model.system.service.UserService;
import com.xiaoke.utils.SecurityUtils;
import io.swagger.annotations.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.Charset;

/**
 * 用户 Controller
 *
 * @author chendengwen
 * @date 2020-07-18 13:05:10
 */
@Api(value = "user", tags = "用户")
@RestController
@Slf4j
@AllArgsConstructor
@RequestMapping("${controller.prefix}/${controller.system.prefix}/user")
public class UserController {
    private final UserService userService;
    private final RedisTemplate redisTemplate;
    private final WxService wxService;
    private static final PasswordEncoder ENCODER = new BCryptPasswordEncoder();

    @ApiOperation("分页查询用户")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "size", value = "每页条数", defaultValue = "10", dataType = "int", paramType = "query", required = true),
            @ApiImplicitParam(name = "current", value = "页数", defaultValue = "1", dataType = "int", paramType = "query", required = true)
    })
    @GetMapping("/page")
    @ValidateGroup(fileds = {
            @ValidateFiled(index = 0, filedName = "current", notNull = true, msg = "缺少current参数！"),
            @ValidateFiled(index = 0, filedName = "size", notNull = true, msg = "缺少size参数")
    })
    public R<IPage> page(Page page, UserDTO userDTO) {
        return R.ok(userService.pageUser(page, userDTO));
    }

    @ApiOperation("获取用户列表")
    @GetMapping("/list")
    public R list(User user) {
        return R.ok(userService.list(Wrappers.emptyWrapper()));
    }

    @ApiOperation("通过ID查询用户")
    @GetMapping("/{id}")
    @Permission("user_user_view")
    public R getById(@ApiParam(name = "id", value = "用户ID", required = true) @PathVariable Integer id) {
        return R.ok(userService.getById(id));
    }

    @ApiOperation("添加用户")
    @SysLog("添加用户")
    @PostMapping
    @ValidateGroup(fileds = {
            @ValidateFiled(index = 0, notNull = true, filedName = "password", msg = "缺少password参数")
    })
    @Permission("user_user_add")
    @Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public R<Boolean> save(@RequestBody UserDTO user) {
        return userService.saveUser(user);
    }

    @ApiOperation("注册用户")
    @SysLog("注册用户")
    @PostMapping("/register")
    @ValidateGroup(fileds = {
            @ValidateFiled(index = 0, notNull = true, filedName = "password", msg = "缺少password参数")
    })
    @Token(value = false)
    @Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public R<Boolean> register(@RequestBody UserDTO user) {
        if (StrUtil.isEmpty(user.getPhoneCode())) {
            return R.failed("请输入手机验证码！");
        }
        Object phoneCodeObj = redisTemplate.opsForValue().get(CacheConstants.PHONE_CODE_KEY + PhoneTypeEnum.REGISTER.getType() + user.getPhone());
        if (phoneCodeObj == null) {
            return R.failed("手机验证码已过期！");
        }
        if (!StrUtil.equals(phoneCodeObj.toString(), user.getPhoneCode())) {
            return R.failed("手机验证码不正确！");
        }
        redisTemplate.delete(CacheConstants.PHONE_CODE_KEY + PhoneTypeEnum.REGISTER.getType() + user.getPhone());

        SysUser sysUser = SecurityUtils.getUser();
        if (sysUser != null) {
            if (StrUtil.isNotEmpty(sysUser.getAvatar()) && StrUtil.isEmpty(user.getAvatar())) {
                user.setAvatar(sysUser.getAvatar());
            }
            if (StrUtil.isNotEmpty(sysUser.getName()) && StrUtil.isEmpty(user.getName())) {
                user.setName(sysUser.getName());
            }
        }
        return userService.saveUser(user);
    }

    @ApiOperation("修改用户")
    @SysLog("修改用户")
    @PutMapping
    @ValidateGroup(fileds = {
            @ValidateFiled(index = 0, notNull = true, filedName = "id", msg = "缺少id参数")
    })
    @Permission("user_user_edit")
    @Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public R<Boolean> update(@RequestBody UserDTO user) {
        log.info("修改用户！id:{}", user.getId());
        return userService.updateUser(user);
    }

    @ApiOperation("修改个人信息")
    @SysLog("修改个人信息")
    @PutMapping("/update/info")
    @Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public R<Boolean> updateInfo(@RequestBody UserDTO user) {
        Integer userId = SecurityUtils.getUser().getId();
        user.setId(userId);
        log.info("修改用户！id:{}", userId);
        return userService.updateUser(user);
    }

    @ApiOperation("修改密码")
    @SysLog("修改密码")
    @ValidateGroup(fileds = {
            @ValidateFiled(index = 0, notNull = true, filedName = "password", msg = "缺少password参数"),
            @ValidateFiled(index = 0, notNull = true, filedName = "newPassword", msg = "缺少newPassword参数")
    })
    @PutMapping("/update/password")
    @Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public R<Boolean> updatePassword(@RequestBody UserDTO user) {

        AES aes = new AES(Mode.CBC, Padding.ZeroPadding,
                new SecretKeySpec(Constant.PASSWORD_SALT.getBytes(), "AES"),
                new IvParameterSpec(Constant.PASSWORD_SALT.getBytes()));
        String password = aes.decryptStr(user.getPassword(), Charset.defaultCharset());
        String newPassword = aes.decryptStr(user.getNewPassword(), Charset.defaultCharset());

        Integer userId = SecurityUtils.getUser().getId();
        User userById = userService.getById(userId);
        //校验原密码
        if (!ENCODER.matches(password, userById.getPassword())) {
            return R.failed(Boolean.FALSE).setMsg("原密码不正确！");
        }
        //修改密码
        User updateUser = new User();
        updateUser.setId(userId);
        updateUser.setPassword(ENCODER.encode(newPassword));
        log.info("用户修改密码！id:{}", userId);
        return R.ok(userService.updateById(updateUser)).setMsg("修改密码成功！");
    }

    @ApiOperation("忘记密码")
    @SysLog("忘记密码")
    @ValidateGroup(fileds = {
            @ValidateFiled(index = 0, notNull = true, filedName = "phone", msg = "缺少phone参数"),
            @ValidateFiled(index = 0, notNull = true, filedName = "phoneCode", msg = "缺少phoneCode参数"),
            @ValidateFiled(index = 0, notNull = true, filedName = "password", msg = "缺少code参数")
    })
    @PutMapping("/forget/password")
    @Token(value = false)
    @Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public R<Boolean> forgetPassword(@RequestBody UserDTO user) {
        Object code = redisTemplate.opsForValue().get(
                CacheConstants.PHONE_CODE_KEY + PhoneTypeEnum.FORGET_PASSWORD.getType() + user.getPhone());
        if (!user.getPhoneCode().equals(code)) {
            return R.failed(Boolean.FALSE).setMsg("短信验证码不正确！");
        }
        redisTemplate.delete(CacheConstants.PHONE_CODE_KEY + PhoneTypeEnum.FORGET_PASSWORD.getType() + user.getPhone());

        QueryWrapper<User> query = Wrappers.query();
        query.lambda().eq(User::getPhone, user.getPhone());
        query.last("LIMIT 1");
        User userTemp = userService.getOne(query);
        if (userTemp == null) {
            return R.failed("手机号未绑定！");
        }
        AES aes = new AES(Mode.CBC, Padding.ZeroPadding,
                new SecretKeySpec(Constant.PASSWORD_SALT.getBytes(), "AES"),
                new IvParameterSpec(Constant.PASSWORD_SALT.getBytes()));
        String password = aes.decryptStr(user.getPassword(), Charset.defaultCharset());
        //修改密码
        User updateUser = new User();
        updateUser.setId(userTemp.getId());
        updateUser.setPassword(ENCODER.encode(password));
        log.info("修改用户密码！id:{}", userTemp.getId());
        return R.ok(userService.updateById(updateUser)).setMsg("重置密码成功！");
    }


    @ApiOperation("修改手机号")
    @SysLog("修改手机号")
    @ValidateGroup(fileds = {
            @ValidateFiled(index = 0, notNull = true, filedName = "phone", msg = "缺少phone参数"),
            @ValidateFiled(index = 0, notNull = true, filedName = "code", msg = "缺少code参数")
    })
    @PutMapping("/update/phone")
    @Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public R<Boolean> updatePhone(@RequestBody UserDTO user) {
        Object code = redisTemplate.opsForValue().get(
                CacheConstants.PHONE_CODE_KEY + PhoneTypeEnum.UPDATE_PHONE.getType() + user.getPhone());
        if (!user.getPhoneCode().equals(code)) {
            return R.failed(Boolean.FALSE).setMsg("短信验证码不正确！");
        }
        Integer userId = SecurityUtils.getUser().getId();
        //修改密码
        User updateUser = new User();
        updateUser.setId(userId);
        updateUser.setPhone(user.getPhone());
        log.info("用户修改手机号！id:{}", userId);
        return R.ok(userService.updateById(updateUser)).setMsg("修改手机号成功！");
    }

    @ApiOperation("解绑微信")
    @SysLog("解绑微信")
    @PutMapping("/relieve/wx-openid")
    @Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public R<Boolean> relieveWxOpenid() {
        Integer userId = SecurityUtils.getUser().getId();
        //清空openid
        User updateUser = new User();
        updateUser.setId(userId);
        updateUser.setWxOpenid("");
        log.info("用户解绑微信！id:{}", userId);
        return R.ok(userService.updateById(updateUser)).setMsg("解绑微信成功！");
    }

    @ApiOperation("删除用户")
    @SysLog("删除用户")
    @DeleteMapping("/{id}")
    @ValidateGroup(fileds = {
            @ValidateFiled(index = 0, notNull = true, msg = "缺少id参数！")
    })
    @Permission("user_user_del")
    @Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public R<Boolean> removeById(@PathVariable Integer id) {
        log.info("删除用户！id:{}", id);
        return R.ok(userService.removeById(id), "删除用户成功！");
    }


    @ApiOperation("重置密码")
    @SysLog("重置密码")
    @ValidateGroup(fileds = {
            @ValidateFiled(index = 0, notNull = true, filedName = "id", msg = "缺少id参数")
    })
    @PutMapping("/reset-password")
    @Permission("user_user_resetPassword")
    @Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public R<Boolean> resetPassword(@RequestBody User model) {
        User user = userService.getById(model);
        if (user == null) {
            return R.failed("用户不存在！");
        }
        AES aes = new AES(Mode.CBC, Padding.ZeroPadding,
                new SecretKeySpec(Constant.PASSWORD_SALT.getBytes(), "AES"),
                new IvParameterSpec(Constant.PASSWORD_SALT.getBytes()));
        String password = aes.decryptStr(model.getPassword(), Charset.defaultCharset());
        //修改密码
        User updateUser = new User();
        updateUser.setId(user.getId());
        updateUser.setPassword(ENCODER.encode(password));
        log.info("重置密码！id:{}", user.getId());
        return R.ok(userService.updateById(updateUser)).setMsg("重置密码成功！");
    }

    @ApiOperation("修改用户miniOpenid")
    @SysLog("修改用户miniOpenid")
    @PutMapping("/update-mini-openid")
    @ValidateGroup(fileds = {
            @ValidateFiled(index = 0, notNull = true, filedName = "miniOpenid", msg = "缺少miniOpenid参数")
    })
    @Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public R<Boolean> updateMiniOpenid(@RequestBody UserDTO model) {
        return userService.updateOpenId(model.getMiniOpenid());
    }


    @ApiOperation("解除用户限制")
    @SysLog("解除用户限制")
    @DeleteMapping("/relieve-limit/{username}")
    @ValidateGroup(fileds = {
            @ValidateFiled(index = 0, notNull = true, msg = "缺少id参数！")
    })
    @Permission("user_user_edit")
    @Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public R relieveLimit(@PathVariable String username) {
        log.info("删除用户！id:{}", username);
        return userService.relieveLimit(username);
    }

    @ApiOperation("获取OpenId")
    @GetMapping("/get-open-id")
    @Token(value = false)
    @ValidateGroup(fileds = {
            @ValidateFiled(index = 0, notNull = true, msg = "缺少code参数！")
    })
    public R<String> getOpenId(String code) {
        return R.ok(wxService.getWxMiniOpenId(code));
    }
}
