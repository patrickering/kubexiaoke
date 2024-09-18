package com.xiaoke.model.kube.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xiaoke.common.core.utils.R;
import com.xiaoke.entity.kube.entity.Template;
import com.xiaoke.entity.kube.vo.TemplateVO;
import com.xiaoke.entity.system.entity.SysUser;
import com.xiaoke.model.kube.mapper.TemplateMapper;
import com.xiaoke.model.kube.service.TemplateService;
import com.xiaoke.utils.JoinUtil;
import com.xiaoke.utils.SecurityUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 模板 Service
 *
 * @author xiaoke
 * @date 2024-08-17 21:23:43
 */
@Service
@AllArgsConstructor
@Slf4j
public class TemplateServiceImpl extends ServiceImpl<TemplateMapper, Template> implements TemplateService {
    private final TemplateMapper templateMapper;

    /**
     * 封装共用查询条件
     *
     * @param template
     * @return
     */
    private QueryWrapper<Template> baseQuery(Template template) {
        QueryWrapper<Template> query = Wrappers.query();
        if (template.getType() != null) {
            query.lambda().eq(Template::getType, template.getType());
        }
        query.orderByDesc("id");
        return query;
    }


    /**
     * 分页查询模板
     *
     * @param page
     * @param template
     * @return
     */
    @Override
    public R<IPage> pageTemplate(Page page, Template template) {
        QueryWrapper<Template> query = this.baseQuery(template);
        if (query == null) {
            return R.ok(new Page<>());
        }
        return R.ok(JoinUtil.page(this.page(page, query), TemplateVO.class));
    }

    /**
     * 获取模板列表
     *
     * @param template
     * @return
     */
    @Override
    public List<TemplateVO> listTemplate(Template template) {
        QueryWrapper<Template> query = this.baseQuery(template);
        if (query == null) {
            return new ArrayList<>();
        }
        return JoinUtil.list(this.list(query), TemplateVO.class);
    }


    /**
     * 通过ID查询模板
     *
     * @param id
     * @return
     */
    @Override
    public TemplateVO getTemplateById(Integer id) {
        return JoinUtil.entity(this.getById(id), TemplateVO.class);
    }

    /**
     * 添加模板
     *
     * @param template
     * @return
     */
    @Override
    public R<Boolean> saveTemplate(Template template) {
        SysUser user = SecurityUtils.getUser();
        template.setCreateBy(user.getId());
        log.info("添加模板！模板:{}", template);
        Boolean saveFlag = this.save(template);
        if (saveFlag) {
            return R.ok(Boolean.TRUE, "添加模板成功！");
        } else {
            return R.ok(Boolean.FALSE, "添加模板失败！");
        }
    }

    /**
     * 修改模板
     *
     * @param template
     * @return
     */
    @Override
    public R<Boolean> updateTemplate(Template template) {
        SysUser user = SecurityUtils.getUser();
        template.setUpdateBy(user.getId());
        log.info("修改模板！id:{}", template.getId());
        UpdateWrapper<Template> updateWrapper = new UpdateWrapper<Template>();
        updateWrapper.lambda().set(Template::getType, template.getType());
        updateWrapper.lambda().eq(Template::getId, template.getId());
        Boolean updateFlag = this.update(template, updateWrapper);
        if (updateFlag) {
            return R.ok(Boolean.TRUE, "修改模板成功！");
        } else {
            return R.ok(Boolean.FALSE, "修改模板失败！");
        }

    }

    /**
     * 删除模板
     *
     * @param id
     * @return
     */
    @Override
    public R<Boolean> removeTemplateById(Integer id) {
        log.info("删除模板！id:{}", id);
        return R.ok(this.removeById(id), "删除模板成功！");
    }


}