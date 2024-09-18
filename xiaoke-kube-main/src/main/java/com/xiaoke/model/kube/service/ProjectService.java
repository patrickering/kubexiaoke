package com.xiaoke.model.kube.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xiaoke.common.core.utils.R;
import com.xiaoke.entity.kube.entity.Project;
import com.xiaoke.entity.kube.vo.ProjectVO;

import java.util.List;

/**
 * 项目 Service
 *
 * @author xiaoke
 * @date 2024-08-03 14:21:50
 */
public interface ProjectService extends IService<Project> {

    /**
     * 分页查询项目
     *
     * @param page
     * @param project
     * @return
     */
    R<IPage> pageProject(Page page, Project project);


    /**
     * 获取项目列表
     *
     * @param project
     * @return
     */
    List<ProjectVO> listProject(Project project);


    /**
     * 通过ID查询项目
     *
     * @param id
     * @return
     */
    ProjectVO getProjectById(Integer id);


    /**
     * 添加项目
     *
     * @param project
     * @return
     */
    R<Boolean> saveProject(Project project);


    /**
     * 修改项目
     *
     * @param project
     * @return
     */
    R<Boolean> updateProject(Project project);

    /**
     * 删除项目
     *
     * @param id
     * @return
     */
    R<Boolean> removeProjectById(Integer id);


}