package com.xiaoke.model.system.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.Mode;
import cn.hutool.crypto.Padding;
import cn.hutool.crypto.symmetric.AES;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xiaoke.common.core.constant.CacheConstants;
import com.xiaoke.common.core.constant.Constant;
import com.xiaoke.common.core.utils.R;
import com.xiaoke.entity.system.dto.UserDTO;
import com.xiaoke.entity.system.entity.SysUser;
import com.xiaoke.entity.system.entity.User;
import com.xiaoke.entity.system.vo.UserVO;
import com.xiaoke.model.system.mapper.UserMapper;
import com.xiaoke.model.system.service.UserService;
import com.xiaoke.properties.LoginProperties;
import com.xiaoke.utils.JoinUtil;
import com.xiaoke.utils.SecurityUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.Charset;
import java.util.List;


@Service
@Slf4j
@AllArgsConstructor
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    private static PasswordEncoder ENCODER = new BCryptPasswordEncoder();
    private final RedisTemplate redisTemplate;
    private final LoginProperties loginProperties;

    /**
     * 分页查询用户信息（含有角色信息）
     *
     * @param page 分页对象
     * @param user 参数列表
     * @return
     */
    @Override
    public IPage pageUser(Page page, UserDTO user) {
        QueryWrapper<User> query = Wrappers.query();
        if (StrUtil.isNotEmpty(user.getUsername())) {
            query.lambda().like(User::getUsername, user.getUsername());
        }

        if (StrUtil.isNotEmpty(user.getName())) {
            query.lambda().like(User::getName, user.getName());
        }

        if (StrUtil.isNotEmpty(user.getPhone())) {
            query.lambda().like(User::getPhone, user.getPhone());
        }

        if (user.getRoleId() != null) {
            query.lambda().in(User::getRoleId, user.getRoleId());
        }

        if (StrUtil.isNotEmpty(user.getEmail())) {
            query.lambda().like(User::getEmail, user.getEmail());
        }

        if (StrUtil.isNotEmpty(user.getLoginFlag())) {
            query.lambda().eq(User::getLoginFlag, user.getLoginFlag());
        }
        if (CollUtil.isNotEmpty(user.getNotId())) {
            query.lambda().notIn(User::getId, user.getNotId());
        }

        Page userPage = JoinUtil.page(this.page(page, query), UserVO.class);
        if (loginProperties.getLimitEnable()) {
            List<UserVO> records = userPage.getRecords();
            if (CollectionUtil.isNotEmpty(records)) {
                for (UserVO record : records) {
                    Object countObj = redisTemplate.opsForValue().get(CacheConstants.LOGIN_LIMIT + record.getUsername());
                    if (countObj != null) {
                        record.setLimitCount(loginProperties.getLimitCount() - (Integer) countObj);
                        if (record.getLimitCount() == 0) {
                            Long expire = redisTemplate.opsForValue().getOperations().getExpire(CacheConstants.LOGIN_LIMIT + record.getUsername());
                            String time = DateUtil.secondToTime(expire.intValue());
                            DateTime dateTime = new DateTime("1970-01-01 " + time, DatePattern.NORM_DATETIME_FORMAT);
                            String format = DateUtil.format(dateTime, "HH小时mm分钟ss秒").replace("00小时", "").replace("00分钟", "").replace("00秒", "");
                            record.setLimitTime(format);
                        }
                    }
                }
                userPage.setRecords(records);
            }
        }
        return userPage;
    }

    /**
     * 修改用户信息
     *
     * @param user
     * @return
     */
    @Override
    public R updateUser(UserDTO user) {
        //验证用户名
        if (StrUtil.isNotBlank(user.getUsername())) {
            if (!checkUsername(user.getId(), user.getUsername())) {
                log.error("修改用户失败，用户名：{}已存在！", user.getUsername());
                return R.failed(Boolean.FALSE).setMsg("用户名或手机号已存在！");
            }
        }
        //验证手机号
        if (StrUtil.isNotBlank(user.getPhone())) {
            if (!checkPhone(user.getId(), user.getPhone())) {
                log.error("修改用户失败，手机号：{}已存在！", user.getPhone());
                return R.failed(Boolean.FALSE).setMsg("手机号已存在！");
            }
        }

        //密码加密
        if (StrUtil.isNotBlank(user.getPassword())) {
            AES aes = new AES(Mode.CBC, Padding.ZeroPadding, new SecretKeySpec(Constant.PASSWORD_SALT.getBytes(), "AES"), new IvParameterSpec(Constant.PASSWORD_SALT.getBytes()));
            String password = aes.decryptStr(user.getPassword(), Charset.defaultCharset());
            user.setPassword(ENCODER.encode(password));
        } else {
            user.setPassword(null);
        }
        if (this.updateById(user)) {
            log.info("修改用户成功，user:{}", user);
            return R.ok(Boolean.TRUE).setMsg("修改用户信息成功！");
        }
        log.error("修改用户失败，user:{}", user);
        return R.failed(Boolean.FALSE).setMsg("修改用户信息失败！");
    }

    /**
     * 添加用户信息
     *
     * @param user
     * @return
     */
    @Override
    public R<Boolean> saveUser(UserDTO user) {
        //验证用户名
        if (StrUtil.isNotBlank(user.getUsername())) {
            if (!checkUsername(null, user.getUsername())) {
                log.error("添加用户失败，用户名：{}已存在！", user.getUsername());
                return R.failed(Boolean.FALSE).setMsg("用户名或手机号已存在！");
            }
        }

        //验证手机号
        if (StrUtil.isNotBlank(user.getPhone())) {
            if (!checkPhone(null, user.getPhone())) {
                log.error("添加用户失败，手机号：{}已存在！", user.getPhone());
                return R.failed(Boolean.FALSE).setMsg("手机号已存在！");
            }
        }

        AES aes = new AES(Mode.CBC, Padding.ZeroPadding, new SecretKeySpec(Constant.PASSWORD_SALT.getBytes(), "AES"), new IvParameterSpec(Constant.PASSWORD_SALT.getBytes()));
        String password = aes.decryptStr(user.getPassword(), Charset.defaultCharset());

        //密码加密
        user.setPassword(ENCODER.encode(password));
        if (this.save(user)) {
            log.info("添加用户成功，user:{}", user);
            return R.ok(Boolean.TRUE).setMsg("添加用户成功！");
        }
        log.error("添加用户失败，user:{}", user);
        return R.failed(Boolean.FALSE).setMsg("添加用户失败！");
    }

    @Override
    public R relieveLimit(String username) {
        redisTemplate.delete(CacheConstants.LOGIN_LIMIT + username);
        return R.ok(Boolean.TRUE, "解除成功！");
    }

    /**
     * 修改用户openId
     *
     * @param openId
     * @return
     */
    @Override
    public R<Boolean> updateOpenId(String openId) {
        SysUser user = SecurityUtils.getUser();
        if (user != null) {
            UpdateWrapper<User> updateWrapper = new UpdateWrapper<User>();
            updateWrapper.lambda().set(User::getMiniOpenid, null);
            updateWrapper.lambda().eq(User::getMiniOpenid, openId);
            this.update(updateWrapper);

            User updateUser = new User();
            updateUser.setId(user.getId());
            updateUser.setMiniOpenid(openId);
            updateUser.updateById();
            return R.ok(Boolean.TRUE, "修改成功！");
        }
        return R.failed("未获取用户信息！");

    }

    /**
     * 验证用户名
     *
     * @param id       用户id
     * @param username 用户名
     * @return
     */
    private Boolean checkUsername(Integer id, String username) {
        User user = new User();
        user.setUsername(username);
        QueryWrapper<User> query = Wrappers.query(user);
        if (id != null) {
            query = query.ne("id", id);
        }
        Long count = this.count(query);
        if (count > 0) {
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }

    /**
     * 验证用户名
     *
     * @return
     */
    private Boolean checkPhone(Integer id, String phone) {
        User user = new User();
        user.setPhone(phone);
        QueryWrapper<User> query = Wrappers.query(user);
        if (id != null) {
            query = query.ne("id", id);
        }
        Long count = this.count(query);
        if (count > 0) {
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }
}
