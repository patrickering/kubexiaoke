package com.xiaoke.model.kube.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xiaoke.common.core.utils.R;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xiaoke.entity.kube.entity.Template;
import java.util.List;
import com.xiaoke.entity.kube.vo.TemplateVO;

/**
* 模板 Service
*
*  @author xiaoke
*  @date 2024-08-17 21:23:43
*/
public interface TemplateService extends IService<Template> {

    /**
     * 分页查询模板
     * @param page
     * @param template
     * @return
     */
    R<IPage> pageTemplate(Page page, Template template);


    /**
     * 获取模板列表
     * @param template
     * @return
     */
    List<TemplateVO> listTemplate(Template template);




    /**
     * 通过ID查询模板
     * @param id
     * @return
     */
     TemplateVO getTemplateById(Integer id);


    /**
     * 添加模板
     * @param template
     * @return
     */
    R<Boolean> saveTemplate(Template template);


    /**
     * 修改模板
     * @param template
     * @return
     */
    R<Boolean> updateTemplate(Template template);


    /**
     * 删除模板
     * @param id
     * @return
     */
    R<Boolean> removeTemplateById(Integer id);





}