package com.xiaoke.model.kube.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xiaoke.common.core.utils.R;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xiaoke.entity.kube.entity.ProjectUser;
import com.xiaoke.entity.kube.vo.ProjectUserVO;

import java.util.List;

/**
* 项目人员 Service
*
*  @author xiaoke
*  @date 2024-08-27 00:01:32
*/
public interface ProjectUserService extends IService<ProjectUser> {

    /**
     * 分页查询项目人员
     * @param page
     * @param projectUser
     * @return
     */
    R<IPage> pageProjectUser(Page page, ProjectUser projectUser);


    /**
     * 获取项目人员列表
     * @param projectUser
     * @return
     */
    List<ProjectUserVO> listProjectUser(ProjectUser projectUser);




    /**
     * 通过ID查询项目人员
     * @param id
     * @return
     */
     ProjectUser getProjectUserById(Integer id);


    /**
     * 添加项目人员
     * @param projectUser
     * @return
     */
    R<Boolean> saveProjectUser(ProjectUser projectUser);


    /**
     * 修改项目人员
     * @param projectUser
     * @return
     */
    R<Boolean> updateProjectUser(ProjectUser projectUser);


    /**
     * 删除项目人员
     * @param id
     * @return
     */
    R<Boolean> removeProjectUserById(Integer id);





}