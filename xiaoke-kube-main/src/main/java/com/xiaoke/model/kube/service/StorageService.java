package com.xiaoke.model.kube.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xiaoke.common.core.utils.R;
import com.xiaoke.entity.kube.entity.Storage;
import com.xiaoke.entity.kube.vo.StorageVO;

import java.util.List;

/**
 * 存储 Service
 *
 * @author xiaoke
 * @date 2024-08-03 17:00:02
 */
public interface StorageService extends IService<Storage> {

    /**
     * 分页查询存储
     *
     * @param page
     * @param storage
     * @return
     */
    R<IPage> pageStorage(Page page, Storage storage);


    /**
     * 获取存储列表
     *
     * @param storage
     * @return
     */
    List<StorageVO> listStorage(Storage storage);


    /**
     * 通过ID查询存储
     *
     * @param id
     * @return
     */
    StorageVO getStorageById(Integer id);


    /**
     * 添加存储
     *
     * @param storage
     * @return
     */
    R<Boolean> saveStorage(Storage storage, String namespace) throws Exception;


    /**
     * 修改存储
     *
     * @param storage
     * @return
     */
    R<Boolean> updateStorage(Storage storage);


    /**
     * 删除存储
     *
     * @param id
     * @return
     */
    R<Boolean> removeStorageById(Integer id);


}