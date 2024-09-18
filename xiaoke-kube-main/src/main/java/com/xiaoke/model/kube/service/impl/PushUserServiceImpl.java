package com.xiaoke.model.kube.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xiaoke.common.core.utils.R;
import com.xiaoke.entity.kube.entity.App;
import com.xiaoke.entity.kube.entity.ProjectUser;
import com.xiaoke.entity.kube.entity.Push;
import com.xiaoke.entity.kube.entity.PushUser;
import com.xiaoke.entity.kube.vo.PushUserVO;
import com.xiaoke.entity.system.entity.SysUser;
import com.xiaoke.entity.system.entity.User;
import com.xiaoke.model.kube.handler.PushHandler;
import com.xiaoke.model.kube.mapper.PushUserMapper;
import com.xiaoke.model.kube.service.ProjectUserService;
import com.xiaoke.model.kube.service.PushService;
import com.xiaoke.model.kube.service.PushUserService;
import com.xiaoke.model.system.service.UserService;
import com.xiaoke.utils.JoinUtil;
import com.xiaoke.utils.SecurityUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 推送接收人 Service
 *
 * @author xiaoke
 * @date 2024-08-28 00:33:59
 */
@Service
@AllArgsConstructor
@Slf4j
public class PushUserServiceImpl extends ServiceImpl<PushUserMapper, PushUser> implements PushUserService {
    private final PushUserMapper pushUserMapper;
    private final ProjectUserService projectUserService;
    private final UserService userService;
    private final PushService pushService;
    private final Map<String, PushHandler> pushHandler;

    /**
     * 封装共用查询条件
     *
     * @param pushUser
     * @return
     */
    private QueryWrapper<PushUser> baseQuery(PushUser pushUser) {
        QueryWrapper<PushUser> query = Wrappers.query();
        if (StrUtil.isNotEmpty(pushUser.getNamespace())) {
            query.lambda().eq(PushUser::getNamespace, pushUser.getNamespace());
        }
        query.orderByDesc("id");
        return query;
    }


    /**
     * 分页查询推送接收人
     *
     * @param page
     * @param pushUser
     * @return
     */
    @Override
    public R<IPage> pagePushUser(Page page, PushUser pushUser) {
        QueryWrapper<PushUser> query = this.baseQuery(pushUser);
        if (query == null) {
            return R.ok(new Page<>());
        }
        return R.ok(JoinUtil.page(this.page(page, query), PushUserVO.class));
    }

    /**
     * 获取推送接收人列表
     *
     * @param pushUser
     * @return
     */
    @Override
    public List<PushUserVO> listPushUser(PushUser pushUser) {
        QueryWrapper<PushUser> query = this.baseQuery(pushUser);
        if (query == null) {
            return new ArrayList<>();
        }
        return JoinUtil.list(this.list(query), PushUserVO.class);
    }


    /**
     * 通过ID查询推送接收人
     *
     * @param id
     * @return
     */
    @Override
    public PushUser getPushUserById(Integer id) {
        return this.getById(id);
    }

    /**
     * 添加推送接收人
     *
     * @param pushUser
     * @return
     */
    @Override
    public R<Boolean> savePushUser(PushUser pushUser) {
        SysUser user = SecurityUtils.getUser();
        pushUser.setCreateBy(user.getId());
        ProjectUser projectUser = projectUserService.getById(pushUser.getProjectUserId());
        if (projectUser == null) {
            return R.failed("用户不存在");
        }
        pushUser.setUserId(projectUser.getUserId());
        log.info("添加推送接收人！推送接收人:{}", pushUser);
        Boolean saveFlag = this.save(pushUser);
        if (saveFlag) {
            return R.ok(Boolean.TRUE, "添加推送接收人成功！");
        } else {
            return R.ok(Boolean.FALSE, "添加推送接收人失败！");
        }
    }

    /**
     * 修改推送接收人
     *
     * @param pushUser
     * @return
     */
    @Override
    public R<Boolean> updatePushUser(PushUser pushUser) {
        SysUser user = SecurityUtils.getUser();
        pushUser.setUpdateBy(user.getId());
        ProjectUser projectUser = projectUserService.getById(pushUser.getProjectUserId());
        if (projectUser == null) {
            return R.failed("用户不存在");
        }
        log.info("修改推送接收人！id:{}", pushUser.getId());
        Boolean updateFlag = this.updateById(pushUser);
        if (updateFlag) {
            return R.ok(Boolean.TRUE, "修改推送接收人成功！");
        } else {
            return R.ok(Boolean.FALSE, "修改推送接收人失败！");
        }

    }

    /**
     * 删除推送接收人
     *
     * @param id
     * @return
     */
    @Override
    public R<Boolean> removePushUserById(Integer id) {
        log.info("删除推送接收人！id:{}", id);
        return R.ok(this.removeById(id), "删除推送接收人成功！");
    }

    @Override
    public Boolean sendAbnormalPush(String namespace, App app, Integer contentType) {
        //获取接收用户
        PushUser pushUserQuery = new PushUser();
        pushUserQuery.setNamespace(namespace);
        pushUserQuery.setContentType(contentType);
        List<PushUser> pushUserList = pushUserQuery.selectList(Wrappers.query(pushUserQuery));
        if (pushUserList != null && pushUserList.size() > 0) {
            for (PushUser pushUser : pushUserList) {
                User user = null;
                try {
                    log.info("==========> 获取用户");
                    user = userService.getById(pushUser.getUserId());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (user != null) {
                    String[] pushTypeArray = pushUser.getPushType().split(",");
                    for (String pushType : pushTypeArray) {
                        //获取推送信息
                        QueryWrapper<Push> queryPush = Wrappers.query();
                        queryPush.lambda().eq(Push::getType, pushType);
                        Push push = pushService.getOne(queryPush);
                        if (push.getOpen()) {
                            pushHandler.get(pushType).send(contentType, push, user, namespace, app);
                        }
                    }
                }

            }
        }
        return null;
    }

}