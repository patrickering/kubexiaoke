package com.xiaoke.model.kube.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xiaoke.common.core.utils.R;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xiaoke.entity.kube.entity.ErrorRecord;
import java.util.List;

/**
* 异常记录 Service
*
*  @author xiaoke
*  @date 2024-08-22 23:06:02
*/
public interface ErrorRecordService extends IService<ErrorRecord> {

    /**
     * 分页查询异常记录
     * @param page
     * @param errorRecord
     * @return
     */
    R<IPage> pageErrorRecord(Page page, ErrorRecord errorRecord);


    /**
     * 获取异常记录列表
     * @param errorRecord
     * @return
     */
    List<ErrorRecord> listErrorRecord(ErrorRecord errorRecord);




    /**
     * 通过ID查询异常记录
     * @param id
     * @return
     */
     ErrorRecord getErrorRecordById(Integer id);


    /**
     * 添加异常记录
     * @param errorRecord
     * @return
     */
    R<Boolean> saveErrorRecord(ErrorRecord errorRecord);


    /**
     * 修改异常记录
     * @param errorRecord
     * @return
     */
    R<Boolean> updateErrorRecord(ErrorRecord errorRecord);


    /**
     * 删除异常记录
     * @param id
     * @return
     */
    R<Boolean> removeErrorRecordById(Integer id);

    /**
    * 批量删除异常记录
    *
    * @param idList
    * @return
    */
    R<Boolean> batchDelete(List<Integer> idList);





    /**
    * 修改已读
    * @param errorRecord
    * @return
    */
    R<Boolean> updateReadFlag(ErrorRecord errorRecord);

}