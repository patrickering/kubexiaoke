package com.xiaoke.model.kube.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xiaoke.common.core.utils.R;
import com.xiaoke.entity.kube.dto.AppDTO;
import com.xiaoke.entity.kube.entity.App;
import com.xiaoke.entity.kube.vo.AppVO;
import io.kubernetes.client.openapi.models.V1Pod;

import java.util.List;
import java.util.Map;

/**
 * 应用 Service
 *
 * @author xiaoke
 * @date 2024-08-03 15:34:01
 */
public interface AppService extends IService<App> {

    /**
     * 分页查询应用
     *
     * @param page
     * @param app
     * @return
     */
    R<IPage> pageApp(Page page, App app);


    /**
     * 获取应用列表
     *
     * @param app
     * @return
     */
    List<AppVO> listApp(App app);

    /**
     * 获取应用pod
     *
     * @param app
     */
    List<V1Pod> getAppPod(App app);


    /**
     * 通过ID查询应用
     *
     * @param id
     * @return
     */
    AppVO getAppById(Integer id);


    /**
     * 添加应用
     *
     * @param app
     * @return
     */
    R<Boolean> saveApp(AppDTO app, String namespace) throws Exception;


    /**
     * 修改应用
     *
     * @param app
     * @return
     */
    R<Boolean> updateApp(AppDTO app) throws Exception;

    /**
     * 创建k8s应用
     *
     * @param appId
     * @throws Exception
     */
    void createKubeApp(Integer appId) throws Exception;

    /**
     * 删除应用
     *
     * @param id
     * @return
     */
    R<Boolean> removeAppById(Integer id);

    /**
     * 停止应用
     *
     * @param id
     */
    Boolean stop(Integer id) throws Exception;

    /**
     * 启动应用
     *
     * @param id
     */
    Boolean start(Integer id, String namespace) throws Exception;


    /**
     * 应用这状态统计
     *
     * @param app
     * @return
     */
    List<Map<String, Object>> stateCount(App app);

    /**
     * 获取占用端口
     *
     * @param namespace
     * @return
     */
    List<Integer> usePort(String namespace);


    /**
     * 修改应用状态
     *
     * @param app
     * @return
     */
    Boolean updateState(AppVO app) throws Exception;

    /**
     * 修改当前应用部署数
     *
     * @param app
     * @return
     */
    Boolean updateNowCount(App app);

    /**
     * 删除pod
     *
     * @param podName
     * @param namespace
     * @return
     */
    Boolean deletePod(String podName, String namespace);


    /**
     * 修改应用镜像
     *
     * @param app
     * @return
     */
    R<Boolean> updateImage(App app) throws Exception;
}