package com.xiaoke.model.kube.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xiaoke.common.core.utils.R;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xiaoke.entity.kube.entity.Namespace;
import java.util.List;
import com.xiaoke.entity.kube.vo.NamespaceVO;

/**
* 命名空间 Service
*
*  @author xiaoke
*  @date 2024-08-03 15:00:35
*/
public interface NamespaceService extends IService<Namespace> {

    /**
     * 分页查询命名空间
     * @param page
     * @param namespace
     * @return
     */
    R<IPage> pageNamespace(Page page, Namespace namespace);


    /**
     * 获取命名空间列表
     * @param namespace
     * @return
     */
    List<NamespaceVO> listNamespace(Namespace namespace);
    /**
     * 获取当前命名空间
     * @param namespace
     * @return
     */
    NamespaceVO currentNamespace(String namespace);




    /**
     * 通过ID查询命名空间
     * @param id
     * @return
     */
     NamespaceVO getNamespaceById(Integer id);


    /**
     * 添加命名空间
     * @param namespace
     * @return
     */
    R<Boolean> saveNamespace(Namespace namespace) throws Exception;


    /**
     * 修改命名空间
     * @param namespace
     * @return
     */
    R<Boolean> updateNamespace(Namespace namespace);


    /**
     * 删除命名空间
     * @param id
     * @return
     */
    R<Boolean> removeNamespaceById(Integer id);





}