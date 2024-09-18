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
import com.xiaoke.entity.kube.entity.AppStorage;
import com.xiaoke.model.kube.mapper.AppStorageMapper;
import com.xiaoke.model.kube.service.AppStorageService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 应用存储 Service
 *
 * @author xiaoke
 * @date 2024-08-03 17:06:23
 */
@Service
@AllArgsConstructor
@Slf4j
public class AppStorageServiceImpl extends ServiceImpl<AppStorageMapper, AppStorage> implements AppStorageService {
    private final AppStorageMapper appStorageMapper;

    /**
     * 封装共用查询条件
     *
     * @param appStorage
     * @return
     */
    private QueryWrapper<AppStorage> baseQuery(AppStorage appStorage) {
        QueryWrapper<AppStorage> query = Wrappers.query();
        if (appStorage.getAppId() != null) {
            query.lambda().eq(AppStorage::getAppId, appStorage.getAppId());
        }
        query.orderByDesc("id");
        return query;
    }


    /**
     * 分页查询应用存储
     *
     * @param page
     * @param appStorage
     * @return
     */
    @Override
    public R<IPage> pageAppStorage(Page page, AppStorage appStorage) {
        QueryWrapper<AppStorage> query = this.baseQuery(appStorage);
        if (query == null) {
            return R.ok(new Page<>());
        }
        return R.ok(this.page(page, query));
    }

    /**
     * 获取应用存储列表
     *
     * @param appStorage
     * @return
     */
    @Override
    public List<AppStorage> listAppStorage(AppStorage appStorage) {
        QueryWrapper<AppStorage> query = this.baseQuery(appStorage);
        if (query == null) {
            return new ArrayList<>();
        }
        return this.list(query);
    }


    /**
     * 通过ID查询应用存储
     *
     * @param id
     * @return
     */
    @Override
    public AppStorage getAppStorageById(Integer id) {
        return this.getById(id);
    }

    /**
     * 添加应用存储
     *
     * @param appStorage
     * @return
     */
    @Override
    public R<Boolean> saveAppStorage(AppStorage appStorage) {
        log.info("添加应用存储！应用存储:{}", appStorage);
        Boolean saveFlag = this.save(appStorage);
        if (saveFlag) {
            return R.ok(Boolean.TRUE, "添加应用存储成功！");
        } else {
            return R.ok(Boolean.FALSE, "添加应用存储失败！");
        }
    }

    /**
     * 修改应用存储
     *
     * @param appStorage
     * @return
     */
    @Override
    public R<Boolean> updateAppStorage(AppStorage appStorage) {
        log.info("修改应用存储！id:{}", appStorage.getId());
        UpdateWrapper<AppStorage> updateWrapper = new UpdateWrapper<AppStorage>();
        updateWrapper.lambda().set(AppStorage::getAppId, appStorage.getAppId());
        updateWrapper.lambda().set(AppStorage::getStorageId, appStorage.getStorageId());
        updateWrapper.lambda().set(AppStorage::getCapacity, appStorage.getCapacity());
        updateWrapper.lambda().eq(AppStorage::getId, appStorage.getId());
        Boolean updateFlag = this.update(appStorage, updateWrapper);
        if (updateFlag) {
            return R.ok(Boolean.TRUE, "修改应用存储成功！");
        } else {
            return R.ok(Boolean.FALSE, "修改应用存储失败！");
        }

    }

    /**
     * 删除应用存储
     *
     * @param id
     * @return
     */
    @Override
    public R<Boolean> removeAppStorageById(Integer id) {
        log.info("删除应用存储！id:{}", id);
        return R.ok(this.removeById(id), "删除应用存储成功！");
    }

    /**
     * 批量保存应用存储信息
     * 此方法首先根据应用ID批量删除现有的存储信息，然后根据提供的应用对象中的存储列表批量添加新的存储信息
     * 主要用于更新应用的存储信息，确保存储信息的准确性和最新性
     *
     * @param app 应用DTO对象，包含应用ID和存储列表等信息
     */
    @Override
    public void batchSave(AppDTO app) {
        // 日志记录，开始批量删除存储信息
        log.info("根据应用批量删除存储：appId:{}", app.getId());

        // 构建查询条件，用于精确删除指定应用ID的存储记录
        QueryWrapper<AppStorage> query = Wrappers.query();
        query.lambda().eq(AppStorage::getAppId, app.getId());

        // 执行删除操作，根据构建的查询条件批量删除存储记录
        this.remove(query);

        // 检查是否有新的存储记录需要添加
        if (CollectionUtil.isNotEmpty(app.getStorageList())) {
            // 日志记录，准备批量添加新的存储记录
            log.info("批量添加应用存储：appId:{}", app.getId());

            // 获取应用的存储列表
            List<AppStorage> storageList = app.getStorageList();
            // 遍历存储列表，设置新的ID和应用ID，准备添加新的存储记录
            for (AppStorage appStorage : storageList) {
                appStorage.setId(null);
                appStorage.setAppId(app.getId());
            }
            // 批量添加新的存储记录
            this.saveBatch(storageList);
        }
    }


}