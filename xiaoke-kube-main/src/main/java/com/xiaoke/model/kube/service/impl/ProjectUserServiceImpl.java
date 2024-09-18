package com.xiaoke.model.kube.service.impl;

import com.xiaoke.entity.kube.vo.ProjectUserVO;
import com.xiaoke.entity.system.entity.SysUser;
import com.xiaoke.utils.JoinUtil;
import com.xiaoke.utils.SecurityUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xiaoke.common.core.utils.R;
import cn.hutool.core.util.StrUtil;
import java.util.List;
import java.util.ArrayList;
import com.xiaoke.entity.kube.entity.ProjectUser;
import com.xiaoke.model.kube.mapper.ProjectUserMapper;
import com.xiaoke.model.kube.service.ProjectUserService;

import java.util.stream.Collectors;

/**
* 项目人员 Service
*
*  @author xiaoke
*  @date 2024-08-27 00:01:32
*/
@Service
@AllArgsConstructor
@Slf4j
public class ProjectUserServiceImpl extends ServiceImpl<ProjectUserMapper,ProjectUser> implements ProjectUserService{
    private final ProjectUserMapper projectUserMapper;

    /**
     * 封装共用查询条件
     * @param projectUser
     * @return
     */
    private QueryWrapper<ProjectUser> baseQuery(ProjectUser projectUser){
        QueryWrapper<ProjectUser> query = Wrappers.query();
        if(projectUser.getProjectId()!=null){
            query.lambda().eq(ProjectUser::getProjectId, projectUser.getProjectId());
        }
        query.orderByDesc("id");
        return query;
    }


    /**
     * 分页查询项目人员
     * @param page
     * @param projectUser
     * @return
     */
    @Override
    public R<IPage> pageProjectUser(Page page, ProjectUser projectUser){
        QueryWrapper<ProjectUser> query = this.baseQuery(projectUser);
        if(query==null){
            return R.ok(new Page<>());
        }
        return R.ok(JoinUtil.page(this.page(page, query), ProjectUserVO.class));
    }

    /**
     * 获取项目人员列表
     * @param projectUser
     * @return
     */
    @Override
    public List<ProjectUserVO> listProjectUser(ProjectUser projectUser){
        QueryWrapper<ProjectUser> query = this.baseQuery(projectUser);
        if(query==null){
            return new ArrayList<>();
        }
        return JoinUtil.list(this.list(query), ProjectUserVO.class);
    }





    /**
     * 通过ID查询项目人员
     * @param id
     * @return
     */
    @Override
    public ProjectUser getProjectUserById(Integer id){
        return this.getById(id);
    }

    /**
     * 添加项目人员
     * @param projectUser
     * @return
     */
    @Override
    public R<Boolean> saveProjectUser(ProjectUser projectUser){
        SysUser user = SecurityUtils.getUser();
        projectUser.setCreateBy(user.getId());
        log.info("添加项目人员！项目人员:{}",projectUser);
        Boolean saveFlag = this.save(projectUser);
        if(saveFlag){
            return R.ok(Boolean.TRUE,"添加项目人员成功！");
        }else{
            return R.ok(Boolean.FALSE,"添加项目人员失败！");
        }
    }

    /**
     * 修改项目人员
     * @param projectUser
     * @return
     */
    @Override
    public R<Boolean> updateProjectUser(ProjectUser projectUser){
        SysUser user = SecurityUtils.getUser();
        projectUser.setUpdateBy(user.getId());
        log.info("修改项目人员！id:{}",projectUser.getId());
        Boolean updateFlag = this.updateById(projectUser);
        if(updateFlag){
            return R.ok(Boolean.TRUE,"修改项目人员成功！");
        }else{
            return R.ok(Boolean.FALSE,"修改项目人员失败！");
        }

    }

    /**
     * 删除项目人员
     * @param id
     * @return
     */
    @Override
    public R<Boolean> removeProjectUserById(Integer id){
        log.info("删除项目人员！id:{}",id);
        return R.ok(this.removeById(id),"删除项目人员成功！");
    }




}