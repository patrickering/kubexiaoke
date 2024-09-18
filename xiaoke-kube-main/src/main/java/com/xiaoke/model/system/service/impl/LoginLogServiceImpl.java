package com.xiaoke.model.system.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xiaoke.common.core.utils.R;
import com.xiaoke.entity.system.entity.LoginLog;
import com.xiaoke.entity.system.entity.SysUser;
import com.xiaoke.entity.system.entity.User;
import com.xiaoke.entity.system.vo.LoginLogVO;
import com.xiaoke.model.system.mapper.LoginLogMapper;
import com.xiaoke.model.system.service.LoginLogService;
import com.xiaoke.utils.JoinUtil;
import com.xiaoke.utils.SecurityUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


/**
 * 登录日志 Service
 *
 * @author xiaoke
 * @date 2022-01-23 15:30:21
 */
@Service
@AllArgsConstructor
@Slf4j
public class LoginLogServiceImpl extends ServiceImpl<LoginLogMapper, LoginLog> implements LoginLogService {
    private final LoginLogMapper loginLogMapper;

    /**
     * 封装共用查询条件
     *
     * @param loginLog
     * @return
     */
    private QueryWrapper<LoginLog> baseQuery(LoginLog loginLog) {
        QueryWrapper<LoginLog> query = Wrappers.query();
        if (StrUtil.isNotEmpty(loginLog.getType())) {
            query.eq("type", loginLog.getType());
        }
        if (StrUtil.isNotEmpty(loginLog.getIp())) {
            query.like("ip", loginLog.getIp());
        }
        if (StrUtil.isNotEmpty(loginLog.getSource())) {
            query.eq("source", loginLog.getSource());
        }
        if (loginLog.getToken() != null) {
            query.like("token", loginLog.getToken());
        }
        query.orderByDesc("id");
        return query;
    }


    /**
     * 分页查询登录日志
     *
     * @param page
     * @param loginLog
     * @return
     */
    @Override
    public R<IPage> pageLoginLog(Page page, LoginLog loginLog) {
        QueryWrapper<LoginLog> query = this.baseQuery(loginLog);
        return R.ok(JoinUtil.page(this.page(page, query), LoginLogVO.class));
    }

    /**
     * 获取登录日志列表
     *
     * @param loginLog
     * @return
     */
    @Override
    public R listLoginLog(LoginLog loginLog) {
        QueryWrapper<LoginLog> query = this.baseQuery(loginLog);
        return R.ok(JoinUtil.list(this.list(query), LoginLogVO.class));
    }


    /**
     * 通过ID查询登录日志
     *
     * @param id
     * @return
     */
    @Override
    public R getLoginLogById(Integer id) {
        return R.ok(JoinUtil.entity(this.getById(id), LoginLogVO.class));
    }

    /**
     * 添加登录日志
     *
     * @param loginLog
     * @return
     */
    @Override
    public R<Boolean> saveLoginLog(LoginLog loginLog) {
        SysUser user = SecurityUtils.getUser();
        loginLog.setCreateBy(user.getId());
        log.info("添加登录日志！登录日志:{}", loginLog);
        return R.ok(this.save(loginLog), "添加登录日志成功！");
    }

    /**
     * 修改登录日志
     *
     * @param loginLog
     * @return
     */
    @Override
    public R<Boolean> updateLoginLog(LoginLog loginLog) {
        User user = SecurityUtils.getUser();
        loginLog.setUpdateBy(user.getId());
        log.info("修改登录日志！id:{}", loginLog.getId());
        return R.ok(this.updateById(loginLog), "修改登录日志成功！");
    }

    /**
     * 删除登录日志
     *
     * @param id
     * @return
     */
    @Override
    public R<Boolean> removeLoginLogById(Integer id) {
        log.info("删除登录日志！id:{}", id);
        return R.ok(this.removeById(id), "删除登录日志成功！");
    }


}