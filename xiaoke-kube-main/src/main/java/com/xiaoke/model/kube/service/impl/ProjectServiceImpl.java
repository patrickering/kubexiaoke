package com.xiaoke.model.kube.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xiaoke.common.core.utils.R;
import com.xiaoke.entity.kube.entity.Project;
import com.xiaoke.entity.kube.entity.ProjectUser;
import com.xiaoke.entity.kube.vo.ProjectVO;
import com.xiaoke.entity.system.entity.SysUser;
import com.xiaoke.model.kube.mapper.ProjectMapper;
import com.xiaoke.model.kube.service.ProjectService;
import com.xiaoke.model.kube.service.ProjectUserService;
import com.xiaoke.utils.JoinUtil;
import com.xiaoke.utils.SecurityUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 项目 Service
 *
 * @author xiaoke
 * @date 2024-08-03 14:21:50
 */
@Service
@AllArgsConstructor
@Slf4j
public class ProjectServiceImpl extends ServiceImpl<ProjectMapper, Project> implements ProjectService {
    private final ProjectMapper projectMapper;
    private final ProjectUserService projectUserService;

    /**
     * 封装共用查询条件
     *
     * @param project
     * @return
     */
    private QueryWrapper<Project> baseQuery(Project project) {
        QueryWrapper<Project> query = Wrappers.query();
        if (StrUtil.isNotEmpty(project.getTitle())) {
            query.lambda().like(Project::getTitle, project.getTitle());
        }
        if (StrUtil.isNotEmpty(project.getRemark())) {
            query.lambda().like(Project::getRemark, project.getRemark());
        }
        if (!SecurityUtils.isAdmin()) {
            QueryWrapper<ProjectUser> queryProjectUser = Wrappers.query();
            queryProjectUser.lambda().eq(ProjectUser::getUserId, SecurityUtils.getUser().getId());
            List<ProjectUser> projectUserList = projectUserService.list(queryProjectUser);
            if (CollUtil.isEmpty(projectUserList)) {
                return null;
            }
            List<Integer> projectIdList = projectUserList.stream().map(ProjectUser::getProjectId).collect(Collectors.toList());
            query.lambda().in(Project::getId, projectIdList);
        }
        query.orderByDesc("id");
        return query;
    }


    /**
     * 分页查询项目
     *
     * @param page
     * @param project
     * @return
     */
    @Override
    public R<IPage> pageProject(Page page, Project project) {
        QueryWrapper<Project> query = this.baseQuery(project);
        if (query == null) {
            return R.ok(new Page<>());
        }
        return R.ok(JoinUtil.page(this.page(page, query), ProjectVO.class));
    }

    /**
     * 获取项目列表
     *
     * @param project
     * @return
     */
    @Override
    public List<ProjectVO> listProject(Project project) {
        QueryWrapper<Project> query = this.baseQuery(project);
        if (query == null) {
            return new ArrayList<>();
        }
        List<ProjectVO> projectList = JoinUtil.list(this.list(query), ProjectVO.class);
        if (CollUtil.isNotEmpty(projectList)) {
            List<Integer> projectIdList = projectList.stream().map(Project::getId).collect(Collectors.toList());
            QueryWrapper<ProjectUser> queryProjectUser = Wrappers.query();
            queryProjectUser.lambda().in(ProjectUser::getProjectId, projectIdList);
            queryProjectUser.lambda().in(ProjectUser::getUserId, SecurityUtils.getUser().getId());
            List<ProjectUser> projectUserList = projectUserService.list(queryProjectUser);
            List<ProjectVO> followList = new ArrayList<>();
            List<ProjectVO> list = new ArrayList<>();
            for (ProjectVO item : projectList) {
                if (CollUtil.isNotEmpty(projectUserList)) {
                    ProjectUser projectUser = projectUserList.stream().filter(projectUserItem -> projectUserItem.getProjectId().equals(item.getId())).findFirst().orElse(null);
                    if (projectUser != null) {
                        item.setProjectUser(projectUser);
                        if (projectUser.getFollowFlag()) {
                            followList.add(item);
                        } else {
                            list.add(item);
                        }
                    } else {
                        list.add(item);
                    }
                } else {
                    list.add(item);
                }
            }
            projectList = followList.stream().sorted(Comparator.comparing(ProjectVO::getCreateTime).reversed()).collect(Collectors.toList());
            projectList.addAll(list);
        }
        return projectList;
    }


    /**
     * 通过ID查询项目
     *
     * @param id
     * @return
     */
    @Override
    public ProjectVO getProjectById(Integer id) {
        return JoinUtil.entity(this.getById(id), ProjectVO.class);
    }

    /**
     * 添加项目
     *
     * @param project
     * @return
     */
    @Override
    public R<Boolean> saveProject(Project project) {
        SysUser user = SecurityUtils.getUser();
        project.setCreateBy(user.getId());
        log.info("添加项目！项目:{}", project);
        Boolean saveFlag = this.save(project);
        if (saveFlag) {
            //添加项目人员
            ProjectUser projectUser = new ProjectUser();
            projectUser.setProjectId(project.getId());
            projectUser.setUserId(user.getId());
            projectUser.setFollowFlag(Boolean.FALSE);
            projectUserService.save(projectUser);
            return R.ok(Boolean.TRUE, "添加项目成功！");
        } else {
            return R.ok(Boolean.FALSE, "添加项目失败！");
        }
    }

    /**
     * 修改项目
     *
     * @param project
     * @return
     */
    @Override
    public R<Boolean> updateProject(Project project) {
        SysUser user = SecurityUtils.getUser();
        project.setUpdateBy(user.getId());
        log.info("修改项目！id:{}", project.getId());
        UpdateWrapper<Project> updateWrapper = new UpdateWrapper<Project>();
        updateWrapper.lambda().eq(Project::getId, project.getId());
        Boolean updateFlag = this.update(project, updateWrapper);
        if (updateFlag) {
            return R.ok(Boolean.TRUE, "修改项目成功！");
        } else {
            return R.ok(Boolean.FALSE, "修改项目失败！");
        }

    }

    /**
     * 删除项目
     *
     * @param id
     * @return
     */
    @Override
    public R<Boolean> removeProjectById(Integer id) {
        log.info("删除项目！id:{}", id);
        return R.ok(this.removeById(id), "删除项目成功！");
    }


}