

package com.xiaoke.model.system.handler;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.Mode;
import cn.hutool.crypto.Padding;
import cn.hutool.crypto.symmetric.AES;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.xiaoke.common.core.constant.CacheConstants;
import com.xiaoke.common.core.constant.Constant;
import com.xiaoke.common.core.utils.R;
import com.xiaoke.entity.system.entity.Login;
import com.xiaoke.entity.system.entity.User;
import com.xiaoke.model.system.service.UserService;
import com.xiaoke.properties.LoginProperties;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.Charset;
import java.util.concurrent.TimeUnit;

/**
 * 用户名密码登录
 *
 * @author xiaoke
 * @date 2018/11/18
 */
@Slf4j
@Component("USERNAME")
@AllArgsConstructor
public class UserNameLoginHandler extends AbstractLoginHandler {
    private final UserService sysUserService;
    private static final PasswordEncoder ENCODER = new BCryptPasswordEncoder();
    private final RedisTemplate redisTemplate;
    private final LoginProperties loginProperties;

    /**
     * 校验验证码
     *
     * @param login 通过用户传入获取唯一标识
     * @return
     */
    @Override
    public R<User> check(Login login) {
        if (StrUtil.isEmpty(login.getUsername())) {
            log.error("帐号密码登录：缺少用户名！");
            return R.failed("缺少用户名！");
        }
        Object codeObj = redisTemplate.opsForValue().get(CacheConstants.DEFAULT_CODE_KEY + login.getRandomStr());
        if (codeObj == null) {
            return R.failed("验证码错误！");
        }
        if (!codeObj.equals(login.getCaptcha())) {
            return R.failed("验证码错误！");
        }

        //密码错误限制
        if (loginProperties.getLimitEnable()) {
            Object countObj = redisTemplate.opsForValue().get(CacheConstants.LOGIN_LIMIT + login.getUsername());
            if (countObj != null) {
                Integer count = (Integer) countObj;
                if (count >= loginProperties.getLimitCount()) {
                    Long expire = redisTemplate.opsForValue().getOperations().getExpire(CacheConstants.LOGIN_LIMIT + login.getUsername());
                    String time = DateUtil.secondToTime(expire.intValue());
                    DateTime dateTime = new DateTime("1970-01-01 "+time, DatePattern.NORM_DATETIME_FORMAT);
                    String format = DateUtil.format(dateTime, "HH小时mm分钟ss秒").replace("00小时","").replace("00分钟","").replace("00秒","");
                    return R.failed("用户已被锁定，剩余时间："+format);
                }
            }
        }
        return R.ok();
    }

    /**
     * 用户名密码登录
     * <p>
     *
     * @param login
     * @return
     */
    @Override
    public String identify(Login login) {
        //无须前置处理,返回用户名
        return login.getUsername();
    }

    /**
     * username 获取用户信息
     *
     * @return
     */
    @Override
    public User info(String username) {
        //通过用户名获取用户信息
        return sysUserService.getOne(Wrappers.<User>query().lambda().eq(User::getUsername, username));
    }

    /**
     * 校验用户信息
     *
     * @param user
     * @param login
     * @return
     */
    @Override
    public R<User> afterHandle(User user, Login login) {
        // 构建前端对应解密AES 因子
        AES aes = new AES(Mode.CBC, Padding.ZeroPadding,
                new SecretKeySpec(Constant.PASSWORD_SALT.getBytes(), "AES"),
                new IvParameterSpec(Constant.PASSWORD_SALT.getBytes()));
        String password = aes.decryptStr(login.getPassword(), Charset.defaultCharset());
        if (user == null) {
            Integer count = this.cacheLoginLimit(login.getUsername());
            log.error("用户名密码登录失败,用户名不存在:{}", login.getUsername());
            return R.failed("用户名或密码错误" + (count == null ? "" : ("，再输错" + count + "次该用户将被锁定" + loginProperties.getLimitTime() + "小时")));
        } else if (!ENCODER.matches(password, user.getPassword())) {
            Integer count = this.cacheLoginLimit(login.getUsername());
            log.error("用户名密码登录失败,用户名密码错误:{}", login.getUsername());
            return R.failed("用户名或密码错误" + (count == null ? "" : ("，" + (count == 0 ? "用户已被锁定" : ("再输错" + count + "次该用户将被锁定" + loginProperties.getLimitTime() + "小时")))));
        } else if (!Constant.STATUS_NORMAL.equals(user.getLoginFlag())) {
            log.error("用户名密码登录失败,用户被禁用:{}", login.getUsername());
            return R.failed("用户被禁用");
        }
        //移除登录次数缓存
        if (loginProperties.getLimitEnable()) {
            redisTemplate.delete(CacheConstants.LOGIN_LIMIT + login.getUsername());
        }
        return R.ok(user);
    }


    /**
     * 缓存登录次数
     *
     * @param username
     */
    private Integer cacheLoginLimit(String username) {
        //密码错误限制
        if (loginProperties.getLimitEnable()) {
            Object countObj = redisTemplate.opsForValue().get(CacheConstants.LOGIN_LIMIT + username);
            Integer count = 0;
            if (countObj != null) {
                count = (Integer) countObj;
            }
            //缓存次数
            redisTemplate.opsForValue().set(CacheConstants.LOGIN_LIMIT + username, ++count, loginProperties.getLimitTime(), TimeUnit.HOURS);

            return loginProperties.getLimitCount() - count;

        }
        return null;
    }
}
