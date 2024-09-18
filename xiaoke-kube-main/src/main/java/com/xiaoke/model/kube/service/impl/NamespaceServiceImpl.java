package com.xiaoke.model.kube.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xiaoke.common.core.utils.R;
import com.xiaoke.entity.kube.entity.App;
import com.xiaoke.entity.kube.entity.Namespace;
import com.xiaoke.entity.kube.vo.NamespaceVO;
import com.xiaoke.entity.system.entity.SysUser;
import com.xiaoke.extend.kube.V1NameSpaceService;
import com.xiaoke.model.kube.mapper.NamespaceMapper;
import com.xiaoke.model.kube.service.AppService;
import com.xiaoke.model.kube.service.NamespaceService;
import com.xiaoke.utils.JoinUtil;
import com.xiaoke.utils.SecurityUtils;
import io.kubernetes.client.openapi.models.V1Namespace;
import io.kubernetes.client.openapi.models.V1ObjectMeta;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 命名空间 Service
 *
 * @author xiaoke
 * @date 2024-08-03 15:00:35
 */
@Service
@AllArgsConstructor
@Slf4j
public class NamespaceServiceImpl extends ServiceImpl<NamespaceMapper, Namespace> implements NamespaceService {
    private final V1NameSpaceService v1NameSpaceService;
    private final AppService appService;

    /**
     * 封装共用查询条件
     *
     * @param namespace
     * @return
     */
    private QueryWrapper<Namespace> baseQuery(Namespace namespace) {
        QueryWrapper<Namespace> query = Wrappers.query();
        if (namespace.getProjectId() != null) {
            query.lambda().eq(Namespace::getProjectId, namespace.getProjectId());
        }
        query.orderByDesc("id");
        return query;
    }


    /**
     * 分页查询命名空间
     *
     * @param page
     * @param namespace
     * @return
     */
    @Override
    public R<IPage> pageNamespace(Page page, Namespace namespace) {
        QueryWrapper<Namespace> query = this.baseQuery(namespace);
        if (query == null) {
            return R.ok(new Page<>());
        }
        return R.ok(JoinUtil.page(this.page(page, query), NamespaceVO.class));
    }

    /**
     * 获取命名空间列表
     *
     * @param namespace
     * @return
     */
    @Override
    public List<NamespaceVO> listNamespace(Namespace namespace) {
        QueryWrapper<Namespace> query = this.baseQuery(namespace);
        if (query == null) {
            return new ArrayList<>();
        }
        List<NamespaceVO> namespaceVOList = JoinUtil.list(this.list(query), NamespaceVO.class);
        for (NamespaceVO namespaceVO : namespaceVOList) {
            //获取k8s命名空间实例
            V1Namespace nameSpaceResult = v1NameSpaceService.getNameSpace(namespaceVO.getSign());
            namespaceVO.setNameSpace(nameSpaceResult);
            //获取命名空间下应用状态数量
            App app = new App();
            app.setNamespace(namespaceVO.getSign());
            List<Map<String, Object>> stateCountList = appService.stateCount(app);

            Integer stateAll = 0;
            Integer exampleCount = 0;
            for (Map<String, Object> stateCount : stateCountList) {
                Integer count = Integer.valueOf(stateCount.get("count").toString());
                Integer example = 0;
                if (stateCount.get("nowCount") != null) {
                    example = Integer.valueOf(stateCount.get("nowCount").toString());
                }
                stateAll += count;
                exampleCount += example;
                if ("20".equals(stateCount.get("state"))) {
                    namespaceVO.setRunApp(count);
                } else if ("60".equals(stateCount.get("state"))) {
                    namespaceVO.setErrorApp(count);
                } else if ("40".equals(stateCount.get("state"))) {
                    namespaceVO.setStopApp(count);
                }
            }
            namespaceVO.setAppCount(stateAll);
            namespaceVO.setExampleCount(exampleCount);

            //分配cpu，内存
            this.getNowCpuMemory(namespaceVO);
        }
        return namespaceVOList;
    }

    /**
     * 根据命名空间和蜂巢标识获取当前命名空间的详细信息
     * <p>
     * 此方法通过接收命名空间和蜂巢的标识符，查询对应的命名空间详细信息，并封装成NamespaceVO对象返回
     * 它首先根据蜂巢标识获取蜂巢信息，然后根据命名空间和蜂巢ID查询特定的命名空间信息，
     * 最后将查询到的命名空间信息转换为NamespaceVO对象，并获取其当前的CPU和内存使用情况
     *
     * @param namespace 命名空间的标识符，用于定位特定的命名空间
     * @return 返回一个NamespaceVO对象，包含命名空间的详细信息及其当前的CPU和内存使用情况
     */
    @Override
    public NamespaceVO currentNamespace(String namespace) {
        // 构建查询条件，用于查询特定的命名空间
        QueryWrapper<Namespace> queryNamespace = Wrappers.query();
        queryNamespace.lambda().eq(Namespace::getSign, namespace);
        // 根据查询条件获取命名空间信息，并转换为NamespaceVO对象
        NamespaceVO namespaceVO = JoinUtil.entity(this.getOne(queryNamespace), NamespaceVO.class);
        // 获取命名空间的当前CPU和内存使用情况
        this.getNowCpuMemory(namespaceVO);
        // 返回命名空间的详细信息
        return namespaceVO;
    }

    /**
     * 获取当前cpu 内存
     *
     * @param namespace
     */
    public void getNowCpuMemory(NamespaceVO namespace) {
        QueryWrapper<App> queryApp = Wrappers.query();
        queryApp.lambda().eq(App::getNamespace, namespace.getSign());
        List<App> all = appService.list(queryApp);
        Double nowCpuLimit = 0.0;
        Double nowCpuRequest = 0.0;
        Double nowMemoryLimit = 0.0;
        Double nowMemoryRequest = 0.0;
        for (App app : all) {
            nowCpuLimit += (app.getCpuLimit() * app.getCount());
            nowCpuRequest += (app.getCpuRequest() * app.getCount());
            if ("Mi".equals(app.getMemoryLimitCompany())) {
                nowMemoryLimit += (Double.valueOf(app.getMemoryLimit() * app.getCount()) / 1024);
            } else if ("Gi".equals(app.getMemoryLimitCompany())) {
                nowMemoryLimit += (app.getMemoryLimit() * app.getCount());
            } else if ("Ti".equals(app.getMemoryLimitCompany())) {
                nowMemoryLimit += (app.getMemoryLimit() * app.getCount() * 1024);
            }

            if ("Mi".equals(app.getMemoryRequestCompany())) {
                nowMemoryRequest += (Double.valueOf(app.getMemoryRequest() * app.getCount()) / 1024);
            }
            if ("Gi".equals(app.getMemoryRequestCompany())) {
                nowMemoryRequest += (app.getMemoryRequest() * app.getCount());
            }
            if ("Ti".equals(app.getMemoryRequestCompany())) {
                nowMemoryRequest += (app.getMemoryRequest() * app.getCount() * 1024);
            }
        }
        namespace.setNowCpuLimit(nowCpuLimit);
        namespace.setNowCpuRequest(nowCpuRequest);
        namespace.setNowMemoryLimit(nowMemoryLimit);
        namespace.setNowMemoryRequest(nowMemoryRequest);
    }

    /**
     * 通过ID查询命名空间
     *
     * @param id
     * @return
     */
    @Override
    public NamespaceVO getNamespaceById(Integer id) {
        NamespaceVO namespace = JoinUtil.entity(this.getById(id), NamespaceVO.class);
        getNowCpuMemory(namespace);
        return namespace;
    }

    /**
     * 添加命名空间
     *
     * @param namespace
     * @return
     */
    @Override
    public R<Boolean> saveNamespace(Namespace namespace) throws Exception {
        QueryWrapper<Namespace> query = Wrappers.query();
        query.lambda().eq(Namespace::getSign, namespace.getSign());
        long count = this.count(query);
        if (count > 0) {
            return R.failed("命名空间已存在!");
        }

        SysUser user = SecurityUtils.getUser();
        namespace.setCreateBy(user.getId());
        log.info("添加命名空间！命名空间:{}", namespace);
        Boolean saveFlag = this.save(namespace);
        if (saveFlag) {
            //创建k8s命名空间
            Boolean nameSpaceFlag = this.createNameSpace(namespace);
            if (!nameSpaceFlag) {
                throw new Exception("创建命名空间失败！");
            }
            return R.ok(Boolean.TRUE, "添加命名空间成功！");
        } else {
            return R.ok(Boolean.FALSE, "添加命名空间失败！");
        }
    }

    /**
     * 创建命名空间
     * <p>
     * 该方法首先尝试根据提供的签名和集群名称查询命名空间如果查询到命名空间，则返回true，
     * 表示命名空间已存在；否则，将创建一个新的命名空间并返回创建结果的成功与否
     *
     * @param namespace 命名空间对象，包含签名等信息
     * @return Boolean 创建命名空间操作的结果，true表示成功，false表示失败
     */
    private Boolean createNameSpace(Namespace namespace) {
        // 尝试根据签名和集群名称获取已存在的命名空间
        V1Namespace v1Namespace = v1NameSpaceService.getNameSpace(namespace.getSign());
        if (v1Namespace != null) {
            // 如果已存在，则直接返回成功
            return Boolean.TRUE;
        }
        // 准备创建新的命名空间
        V1Namespace nameSpace = new V1Namespace();
        // 设置命名空间的元数据
        V1ObjectMeta nameSpaceMetadata = new V1ObjectMeta();
        nameSpaceMetadata.setName(namespace.getSign());
        // 设置命名空间的标签，便于识别
        Map<String, String> nameSpaceMetadataLabels = new HashMap<>();
        nameSpaceMetadataLabels.put("name", namespace.getSign());
        nameSpaceMetadata.setLabels(nameSpaceMetadataLabels);
        nameSpace.setMetadata(nameSpaceMetadata);
        // 执行创建命名空间操作
        V1Namespace result = v1NameSpaceService.createNameSpace(nameSpace);
        // 根据创建结果判断是否成功
        if (result != null && result.getStatus() != null) {
            return Boolean.TRUE;
        }
        // 如果创建失败，则返回false
        return Boolean.FALSE;
    }


    /**
     * 修改命名空间
     *
     * @param namespace
     * @return
     */
    @Override
    public R<Boolean> updateNamespace(Namespace namespace) {
        SysUser user = SecurityUtils.getUser();
        namespace.setUpdateBy(user.getId());
        log.info("修改命名空间！id:{}", namespace.getId());
        Boolean updateFlag = this.updateById(namespace);
        if (updateFlag) {
            return R.ok(Boolean.TRUE, "修改命名空间成功！");
        } else {
            return R.ok(Boolean.FALSE, "修改命名空间失败！");
        }

    }

    /**
     * 删除命名空间
     *
     * @param id
     * @return
     */
    @Override
    public R<Boolean> removeNamespaceById(Integer id) {
        Namespace namespace = this.getById(id);
        //删除k8s命名空间
        Boolean result = v1NameSpaceService.deleteNameSpace(namespace.getSign());
        if (!result) {
            return R.failed("删除命名空间失败");
        }
        log.info("删除命名空间！id:{}", id);
        return R.ok(this.removeById(id), "删除命名空间成功！");
    }


}