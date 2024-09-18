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
import com.xiaoke.entity.kube.dto.AppServiceDTO;
import com.xiaoke.entity.kube.entity.AppPort;
import com.xiaoke.entity.kube.entity.AppService;
import com.xiaoke.model.kube.mapper.AppServiceMapper;
import com.xiaoke.model.kube.service.AppPortService;
import com.xiaoke.model.kube.service.AppServiceService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 应用Service Service
 *
 * @author xiaoke
 * @date 2024-08-03 16:39:45
 */
@Service
@AllArgsConstructor
@Slf4j
public class AppServiceServiceImpl extends ServiceImpl<AppServiceMapper, AppService> implements AppServiceService {
    private final AppServiceMapper appServiceMapper;
    private final AppPortService appPortService;

    /**
     * 封装共用查询条件
     *
     * @param appService
     * @return
     */
    private QueryWrapper<AppService> baseQuery(AppService appService) {
        QueryWrapper<AppService> query = Wrappers.query();
        if (appService.getAppId() != null) {
            query.lambda().eq(AppService::getAppId, appService.getAppId());
        }
        query.orderByDesc("id");
        return query;
    }


    /**
     * 分页查询应用Service
     *
     * @param page
     * @param appService
     * @return
     */
    @Override
    public R<IPage> pageAppService(Page page, AppService appService) {
        QueryWrapper<AppService> query = this.baseQuery(appService);
        if (query == null) {
            return R.ok(new Page<>());
        }
        return R.ok(this.page(page, query));
    }

    /**
     * 获取应用Service列表
     *
     * @param appService
     * @return
     */
    @Override
    public List<AppService> listAppService(AppService appService) {
        QueryWrapper<AppService> query = this.baseQuery(appService);
        if (query == null) {
            return new ArrayList<>();
        }
        return this.list(query);
    }


    /**
     * 通过ID查询应用Service
     *
     * @param id
     * @return
     */
    @Override
    public AppService getAppServiceById(Integer id) {
        return this.getById(id);
    }

    /**
     * 添加应用Service
     *
     * @param appService
     * @return
     */
    @Override
    public R<Boolean> saveAppService(AppService appService) {
        log.info("添加应用Service！应用Service:{}", appService);
        Boolean saveFlag = this.save(appService);
        if (saveFlag) {
            return R.ok(Boolean.TRUE, "添加应用Service成功！");
        } else {
            return R.ok(Boolean.FALSE, "添加应用Service失败！");
        }
    }

    /**
     * 修改应用Service
     *
     * @param appService
     * @return
     */
    @Override
    public R<Boolean> updateAppService(AppService appService) {
        log.info("修改应用Service！id:{}", appService.getId());
        UpdateWrapper<AppService> updateWrapper = new UpdateWrapper<AppService>();
        updateWrapper.lambda().set(AppService::getAppId, appService.getAppId());
        updateWrapper.lambda().eq(AppService::getId, appService.getId());
        Boolean updateFlag = this.update(appService, updateWrapper);
        if (updateFlag) {
            return R.ok(Boolean.TRUE, "修改应用Service成功！");
        } else {
            return R.ok(Boolean.FALSE, "修改应用Service失败！");
        }

    }

    /**
     * 删除应用Service
     *
     * @param id
     * @return
     */
    @Override
    public R<Boolean> removeAppServiceById(Integer id) {
        log.info("删除应用Service！id:{}", id);
        return R.ok(this.removeById(id), "删除应用Service成功！");
    }

    /**
     * 批量保存应用信息
     * 此方法首先会根据应用ID删除现有的服务和端口信息，然后重新保存新的应用服务和端口信息
     * 主要用于更新应用、服务和端口信息时
     *
     * @param app 应用数据传输对象，包含应用及其关联服务和端口的信息
     */
    @Override
    public void batchSave(AppDTO app) {
        // 构建查询条件，用于查询当前应用下的所有服务
        QueryWrapper<AppService> queryAppService = Wrappers.query();
        queryAppService.lambda().eq(AppService::getAppId, app.getId());
        // 查询当前应用下的所有服务
        List<AppService> appServiceList = this.list(queryAppService);
        // 如果服务列表不为空，则进行删除操作
        if (CollUtil.isNotEmpty(appServiceList)) {
            for (AppService appService : appServiceList) {
                // 构建查询条件，用于查询当前服务下的所有端口
                QueryWrapper<AppPort> query = Wrappers.query();
                query.lambda().eq(AppPort::getServiceId, appService.getId());
                // 删除当前服务下的所有端口
                appPortService.remove(query);
                // 删除当前服务
                this.removeById(appService.getId());
            }
        }
        // 获取新的应用服务列表
        List<AppServiceDTO> serviceList = app.getServiceList();
        for (AppServiceDTO appService : serviceList) {
            // 重置服务ID，以便于保存新的记录
            appService.setId(null);
            // 设置应用ID，关联应用
            appService.setAppId(app.getId());
            // 保存新的服务信息
            this.save(appService);
            // 如果服务下的端口列表不为空，则进行端口保存操作
            if (CollUtil.isNotEmpty(appService.getPortList())) {
                for (AppPort appPort : appService.getPortList()) {
                    // 重置端口ID，以便于保存新的记录
                    appPort.setId(null);
                    // 设置服务ID，关联服务
                    appPort.setServiceId(appService.getId());
                    // 如果服务的访问类型为"ClusterIP"，则端口的节点信息置为空
                    if ("ClusterIP".equals(appService.getVisitType())) {
                        appPort.setNode(null);
                    }
                }
                // 批量保存新的端口信息
                appPortService.saveBatch(appService.getPortList());
            }
        }
    }


}