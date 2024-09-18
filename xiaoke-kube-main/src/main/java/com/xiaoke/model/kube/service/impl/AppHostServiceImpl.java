package com.xiaoke.model.kube.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xiaoke.common.core.utils.R;
import com.xiaoke.entity.kube.dto.AppDTO;
import com.xiaoke.entity.kube.entity.AppHost;
import com.xiaoke.model.kube.mapper.AppHostMapper;
import com.xiaoke.model.kube.service.AppHostService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 应用Host Service
 *
 * @author xiaoke
 * @date 2024-08-03 17:10:09
 */
@Service
@AllArgsConstructor
@Slf4j
public class AppHostServiceImpl extends ServiceImpl<AppHostMapper, AppHost> implements AppHostService {
    private final AppHostMapper appHostMapper;

    /**
     * 封装共用查询条件
     *
     * @param appHost
     * @return
     */
    private QueryWrapper<AppHost> baseQuery(AppHost appHost) {
        QueryWrapper<AppHost> query = Wrappers.query();
        if (appHost.getAppId() != null) {
            query.lambda().eq(AppHost::getAppId, appHost.getAppId());
        }
        query.orderByDesc("id");
        return query;
    }


    /**
     * 分页查询应用Host
     *
     * @param page
     * @param appHost
     * @return
     */
    @Override
    public R<IPage> pageAppHost(Page page, AppHost appHost) {
        QueryWrapper<AppHost> query = this.baseQuery(appHost);
        if (query == null) {
            return R.ok(new Page<>());
        }
        return R.ok(this.page(page, query));
    }

    /**
     * 获取应用Host列表
     *
     * @param appHost
     * @return
     */
    @Override
    public List<AppHost> listAppHost(AppHost appHost) {
        QueryWrapper<AppHost> query = this.baseQuery(appHost);
        if (query == null) {
            return new ArrayList<>();
        }
        return this.list(query);
    }


    /**
     * 通过ID查询应用Host
     *
     * @param id
     * @return
     */
    @Override
    public AppHost getAppHostById(Integer id) {
        return this.getById(id);
    }

    /**
     * 添加应用Host
     *
     * @param appHost
     * @return
     */
    @Override
    public R<Boolean> saveAppHost(AppHost appHost) {
        log.info("添加应用Host！应用Host:{}", appHost);
        Boolean saveFlag = this.save(appHost);
        if (saveFlag) {
            return R.ok(Boolean.TRUE, "添加应用Host成功！");
        } else {
            return R.ok(Boolean.FALSE, "添加应用Host失败！");
        }
    }

    /**
     * 修改应用Host
     *
     * @param appHost
     * @return
     */
    @Override
    public R<Boolean> updateAppHost(AppHost appHost) {
        log.info("修改应用Host！id:{}", appHost.getId());
        UpdateWrapper<AppHost> updateWrapper = new UpdateWrapper<AppHost>();
        updateWrapper.lambda().set(AppHost::getAppId, appHost.getAppId());
        updateWrapper.lambda().eq(AppHost::getId, appHost.getId());
        Boolean updateFlag = this.update(appHost, updateWrapper);
        if (updateFlag) {
            return R.ok(Boolean.TRUE, "修改应用Host成功！");
        } else {
            return R.ok(Boolean.FALSE, "修改应用Host失败！");
        }

    }

    /**
     * 删除应用Host
     *
     * @param id
     * @return
     */
    @Override
    public R<Boolean> removeAppHostById(Integer id) {
        log.info("删除应用Host！id:{}", id);
        return R.ok(this.removeById(id), "删除应用Host成功！");
    }

    /**
     * 批量保存应用Host信息
     * 此方法首先根据应用ID批量删除已存在的Host信息，然后根据应用的新Host列表批量添加Host信息
     *
     * @param app 应用DTO对象，包含应用信息和Host列表
     */
    @Override
    public void batchSave(AppDTO app) {
        // 根据应用ID批量删除已存在的Host信息
        log.info("根据应用批量删除HOST：appId:{}", app.getId());
        QueryWrapper<AppHost> query = Wrappers.query();
        query.lambda().eq(AppHost::getAppId, app.getId());
        this.remove(query);

        // 如果应用的新Host列表不为空，则批量添加新的Host信息
        if (CollUtil.isNotEmpty(app.getHostList())) {
            log.info("批量添加应用HOST：appId:{}", app.getId());
            // 遍历Host列表，设置HostID为null，并关联当前应用ID
            for (AppHost appHost : app.getHostList()) {
                appHost.setId(null);
                appHost.setAppId(app.getId());
            }
            // 批量添加新的Host信息
            this.saveBatch(app.getHostList());
        }
    }


}