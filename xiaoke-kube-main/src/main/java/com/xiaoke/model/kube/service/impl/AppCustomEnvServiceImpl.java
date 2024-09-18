package com.xiaoke.model.kube.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xiaoke.common.core.utils.R;
import com.xiaoke.entity.kube.dto.AppDTO;
import com.xiaoke.entity.kube.entity.AppCustomEnv;
import com.xiaoke.model.kube.mapper.AppCustomEnvMapper;
import com.xiaoke.model.kube.service.AppCustomEnvService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 应用环境变量 Service
 *
 * @author xiaoke
 * @date 2024-08-03 15:45:33
 */
@Service
@AllArgsConstructor
@Slf4j
public class AppCustomEnvServiceImpl extends ServiceImpl<AppCustomEnvMapper, AppCustomEnv> implements AppCustomEnvService {
    private final AppCustomEnvMapper appCustomEnvMapper;

    /**
     * 封装共用查询条件
     *
     * @param appCustomEnv
     * @return
     */
    private QueryWrapper<AppCustomEnv> baseQuery(AppCustomEnv appCustomEnv) {
        QueryWrapper<AppCustomEnv> query = Wrappers.query();
        if (appCustomEnv.getAppId() != null) {
            query.lambda().eq(AppCustomEnv::getAppId, appCustomEnv.getAppId());
        }
        query.orderByAsc("id");
        return query;
    }


    /**
     * 分页查询应用环境变量
     *
     * @param page
     * @param appCustomEnv
     * @return
     */
    @Override
    public R<IPage> pageAppCustomEnv(Page page, AppCustomEnv appCustomEnv) {
        QueryWrapper<AppCustomEnv> query = this.baseQuery(appCustomEnv);
        if (query == null) {
            return R.ok(new Page<>());
        }
        return R.ok(this.page(page, query));
    }

    /**
     * 获取应用环境变量列表
     *
     * @param appCustomEnv
     * @return
     */
    @Override
    public List<AppCustomEnv> listAppCustomEnv(AppCustomEnv appCustomEnv) {
        QueryWrapper<AppCustomEnv> query = this.baseQuery(appCustomEnv);
        if (query == null) {
            return new ArrayList<>();
        }
        return this.list(query);
    }


    /**
     * 通过ID查询应用环境变量
     *
     * @param id
     * @return
     */
    @Override
    public AppCustomEnv getAppCustomEnvById(Integer id) {
        return this.getById(id);
    }

    /**
     * 添加应用环境变量
     *
     * @param appCustomEnv
     * @return
     */
    @Override
    public R<Boolean> saveAppCustomEnv(AppCustomEnv appCustomEnv) {
        log.info("添加应用环境变量！应用环境变量:{}", appCustomEnv);
        Boolean saveFlag = this.save(appCustomEnv);
        if (saveFlag) {
            return R.ok(Boolean.TRUE, "添加应用环境变量成功！");
        } else {
            return R.ok(Boolean.FALSE, "添加应用环境变量失败！");
        }
    }

    /**
     * 修改应用环境变量
     *
     * @param appCustomEnv
     * @return
     */
    @Override
    public R<Boolean> updateAppCustomEnv(AppCustomEnv appCustomEnv) {
        log.info("修改应用环境变量！id:{}", appCustomEnv.getId());
        UpdateWrapper<AppCustomEnv> updateWrapper = new UpdateWrapper<AppCustomEnv>();
        updateWrapper.lambda().set(AppCustomEnv::getAppId, appCustomEnv.getAppId());
        updateWrapper.lambda().eq(AppCustomEnv::getId, appCustomEnv.getId());
        Boolean updateFlag = this.update(appCustomEnv, updateWrapper);
        if (updateFlag) {
            return R.ok(Boolean.TRUE, "修改应用环境变量成功！");
        } else {
            return R.ok(Boolean.FALSE, "修改应用环境变量失败！");
        }

    }

    /**
     * 删除应用环境变量
     *
     * @param id
     * @return
     */
    @Override
    public R<Boolean> removeAppCustomEnvById(Integer id) {
        log.info("删除应用环境变量！id:{}", id);
        return R.ok(this.removeById(id), "删除应用环境变量成功！");
    }


    /**
     * 批量保存环境变量
     * <p>
     * 该方法首先根据应用ID批量删除现有的环境变量，然后根据传入的应用信息中的自定义环境变量列表进行批量添加
     * 主要用于更新应用的环境变量，确保数据库中的环境变量与传入的应用信息保持一致
     *
     * @param app 应用信息，包含应用ID和自定义环境变量列表
     */
    @Override
    public void batchSave(AppDTO app) {
        // 根据应用ID删除现有的环境变量
        log.info("根据应用批量删除环境变量：appId:{}", app.getId());
        QueryWrapper<AppCustomEnv> query = Wrappers.query();
        query.lambda().eq(AppCustomEnv::getAppId, app.getId());
        this.remove(query);

        // 如果传入的应用信息包含自定义环境变量列表，则批量添加这些环境变量
        if (CollectionUtil.isNotEmpty(app.getCustomEnvList())) {
            log.info("批量添加应用环境变量：appId:{}", app.getId());
            // 遍历自定义环境变量列表，设置环境变量的ID为null，确保是新记录，并设置应用ID
            for (AppCustomEnv customEnv : app.getCustomEnvList()) {
                customEnv.setId(null);
                customEnv.setAppId(app.getId());
            }
            // 批量保存新的环境变量
            this.saveBatch(app.getCustomEnvList());
        }
    }


}