package com.xiaoke.model.kube.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xiaoke.common.core.utils.R;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xiaoke.entity.kube.entity.AppImageRecord;
import java.util.List;
import com.xiaoke.entity.kube.vo.AppImageRecordVO;

/**
* 应用镜像更新记录 Service
*
*  @author xiaoke
*  @date 2024-08-14 00:03:31
*/
public interface AppImageRecordService extends IService<AppImageRecord> {

    /**
     * 分页查询应用镜像更新记录
     * @param page
     * @param appImageRecord
     * @return
     */
    R<IPage> pageAppImageRecord(Page page, AppImageRecord appImageRecord);


    /**
     * 获取应用镜像更新记录列表
     * @param appImageRecord
     * @return
     */
    List<AppImageRecordVO> listAppImageRecord(AppImageRecord appImageRecord);

    /**
     * 通过ID查询应用镜像更新记录
     * @param id
     * @return
     */
     AppImageRecordVO getAppImageRecordById(Integer id);

    /**
     * 回滚镜像
     * @param id
     * @return
     */
    R<Boolean> goBack(Integer id) throws Exception;





}