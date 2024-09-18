package com.xiaoke.model.kube.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xiaoke.common.core.utils.R;
import com.xiaoke.entity.kube.entity.Storage;
import com.xiaoke.entity.kube.vo.StorageVO;
import com.xiaoke.entity.system.entity.SysUser;
import com.xiaoke.extend.kube.PVCService;
import com.xiaoke.model.kube.mapper.StorageMapper;
import com.xiaoke.model.kube.service.StorageService;
import com.xiaoke.utils.JoinUtil;
import com.xiaoke.utils.SecurityUtils;
import io.kubernetes.client.custom.Quantity;
import io.kubernetes.client.openapi.models.V1ObjectMeta;
import io.kubernetes.client.openapi.models.V1PersistentVolumeClaim;
import io.kubernetes.client.openapi.models.V1PersistentVolumeClaimSpec;
import io.kubernetes.client.openapi.models.V1ResourceRequirements;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 存储 Service
 *
 * @author xiaoke
 * @date 2024-08-03 17:00:02
 */
@Service
@AllArgsConstructor
@Slf4j
public class StorageServiceImpl extends ServiceImpl<StorageMapper, Storage> implements StorageService {
    private final StorageMapper storageMapper;
    private final PVCService pvcService;

    /**
     * 封装共用查询条件
     *
     * @param storage
     * @return
     */
    private QueryWrapper<Storage> baseQuery(Storage storage) {
        QueryWrapper<Storage> query = Wrappers.query();
        if (StrUtil.isNotEmpty(storage.getNamespace())) {
            query.lambda().eq(Storage::getNamespace, storage.getNamespace());
        }
        if (StrUtil.isNotEmpty(storage.getName())) {
            query.lambda().like(Storage::getName, storage.getName());
        }
        query.orderByDesc("id");
        return query;
    }


    /**
     * 分页查询存储
     *
     * @param page
     * @param storage
     * @return
     */
    @Override
    public R<IPage> pageStorage(Page page, Storage storage) {
        QueryWrapper<Storage> query = this.baseQuery(storage);
        if (query == null) {
            return R.ok(new Page<>());
        }
        return R.ok(JoinUtil.page(this.page(page, query), StorageVO.class));
    }

    /**
     * 获取存储列表
     *
     * @param storage
     * @return
     */
    @Override
    public List<StorageVO> listStorage(Storage storage) {
        QueryWrapper<Storage> query = this.baseQuery(storage);
        if (query == null) {
            return new ArrayList<>();
        }
        return JoinUtil.list(this.list(query), StorageVO.class);
    }


    /**
     * 通过ID查询存储
     *
     * @param id
     * @return
     */
    @Override
    public StorageVO getStorageById(Integer id) {
        return JoinUtil.entity(this.getById(id), StorageVO.class);
    }

    /**
     * 添加存储
     *
     * @param storage
     * @return
     */
    @Override
    public R<Boolean> saveStorage(Storage storage, String namespace) throws Exception {
        //验证是否重复
        QueryWrapper<Storage> queryCount = Wrappers.query();
        queryCount.lambda().eq(Storage::getNamespace, namespace);
        queryCount.lambda().eq(Storage::getName, storage.getName());

        long count = this.count(queryCount);
        if (count > 0) {
            return R.failed("存储名称已存在！");
        }
        storage.setNamespace(namespace);
        SysUser user = SecurityUtils.getUser();
        storage.setCreateBy(user.getId());
        log.info("添加存储！存储:{}", storage);
        Boolean saveFlag = this.save(storage);
        if (saveFlag) {
            //k8s创建存储
            Boolean pvc = createPVC(storage);
            if (!pvc) {
                throw new Exception("创建存储失败！");
            }
            return R.ok(Boolean.TRUE, "添加存储成功！");
        } else {
            return R.ok(Boolean.FALSE, "添加存储失败！");
        }
    }

    /**
     * 修改存储
     *
     * @param storage
     * @return
     */
    @Override
    public R<Boolean> updateStorage(Storage storage) {
        SysUser user = SecurityUtils.getUser();
        storage.setUpdateBy(user.getId());
        log.info("修改存储！id:{}", storage.getId());
        UpdateWrapper<Storage> updateWrapper = new UpdateWrapper<Storage>();
        updateWrapper.lambda().set(Storage::getCapacity, storage.getCapacity());
        updateWrapper.lambda().eq(Storage::getId, storage.getId());
        Boolean updateFlag = this.update(storage, updateWrapper);
        if (updateFlag) {
            return R.ok(Boolean.TRUE, "修改存储成功！");
        } else {
            return R.ok(Boolean.FALSE, "修改存储失败！");
        }

    }

    /**
     * 删除存储
     *
     * @param id
     * @return
     */
    @Override
    public R<Boolean> removeStorageById(Integer id) {
        Storage storage = this.getById(id);
        Boolean pvc = pvcService.deletePVC(storage.getNamespace() + "-" + storage.getName(), storage.getNamespace());
        if (!pvc) {
            return R.failed("删除失败！");
        }
        log.info("删除存储！id:{}", id);
        return R.ok(this.removeById(id), "删除存储成功！");
    }


    /**
     * 创建存储
     *
     * @param storage
     * @return
     */
    private Boolean createPVC(Storage storage) {
        V1PersistentVolumeClaim pvc = new V1PersistentVolumeClaim();


        V1ObjectMeta pvcMetadata = new V1ObjectMeta();
        pvcMetadata.setName(storage.getNamespace() + "-" + storage.getName());
        pvc.setMetadata(pvcMetadata);

        V1PersistentVolumeClaimSpec pvcSpec = new V1PersistentVolumeClaimSpec();
        pvcSpec.setStorageClassName("managed-nfs-storage");

        List<String> pvcSpecAccessModesList = new ArrayList<>();
        pvcSpecAccessModesList.add(storage.getStrategy());
        pvcSpec.setAccessModes(pvcSpecAccessModesList);

        V1ResourceRequirements pvcSpecResources = new V1ResourceRequirements();

        Map<String, Quantity> pvcSpecResourcesRequests = new HashMap<>();
        pvcSpecResourcesRequests.put("storage", new Quantity(new BigDecimal(storage.getCapacity()), Quantity.Format.DECIMAL_SI));
        pvcSpecResources.setRequests(pvcSpecResourcesRequests);
        pvcSpec.setResources(pvcSpecResources);
        pvc.setSpec(pvcSpec);
        return pvcService.createPVC(pvc, storage.getNamespace());
    }

}