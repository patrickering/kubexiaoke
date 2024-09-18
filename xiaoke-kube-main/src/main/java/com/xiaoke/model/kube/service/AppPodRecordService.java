package com.xiaoke.model.kube.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xiaoke.common.core.utils.R;
import com.xiaoke.entity.kube.dto.AppPodRecordDTO;
import com.xiaoke.entity.kube.entity.AppPodRecord;

import java.util.List;

/**
 * 应用pod记录 Service
 *
 * @author xiaoke
 * @date 2024-08-14 23:23:56
 */
public interface AppPodRecordService extends IService<AppPodRecord> {

    /**
     * 获取应用pod记录列表
     *
     * @param appPodRecord
     * @return
     */
    List<AppPodRecord> listAppPodRecord(AppPodRecord appPodRecord);


    /**
     * 添加应用pod记录
     *
     * @param appPodRecord
     * @return
     */
    R<Boolean> saveAppPodRecord(AppPodRecord appPodRecord);


    /**
     * pod 日志
     *
     * @param page
     * @param appPodRecord
     * @param namespace
     * @return
     */
    R<IPage> podHistoryLog(Page page, AppPodRecordDTO appPodRecord, String namespace);

}