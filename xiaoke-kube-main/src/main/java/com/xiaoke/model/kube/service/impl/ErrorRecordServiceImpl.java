package com.xiaoke.model.kube.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xiaoke.common.core.utils.R;
import com.xiaoke.entity.kube.entity.ErrorRecord;
import com.xiaoke.entity.system.entity.SysUser;
import com.xiaoke.model.kube.mapper.ErrorRecordMapper;
import com.xiaoke.model.kube.service.ErrorRecordService;
import com.xiaoke.utils.SecurityUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 异常记录 Service
 *
 * @author xiaoke
 * @date 2024-08-22 23:06:02
 */
@Service
@AllArgsConstructor
@Slf4j
public class ErrorRecordServiceImpl extends ServiceImpl<ErrorRecordMapper, ErrorRecord> implements ErrorRecordService {
    private final ErrorRecordMapper errorRecordMapper;

    /**
     * 封装共用查询条件
     *
     * @param errorRecord
     * @return
     */
    private QueryWrapper<ErrorRecord> baseQuery(ErrorRecord errorRecord) {
        QueryWrapper<ErrorRecord> query = Wrappers.query();
        if (StrUtil.isNotEmpty(errorRecord.getTitle())) {
            query.lambda().like(ErrorRecord::getTitle, errorRecord.getTitle());
        }
        if (StrUtil.isNotEmpty(errorRecord.getNamespace())) {
            query.lambda().eq(ErrorRecord::getNamespace, errorRecord.getNamespace());
        }
        query.orderByDesc("id");
        return query;
    }


    /**
     * 分页查询异常记录
     *
     * @param page
     * @param errorRecord
     * @return
     */
    @Override
    public R<IPage> pageErrorRecord(Page page, ErrorRecord errorRecord) {
        QueryWrapper<ErrorRecord> query = this.baseQuery(errorRecord);
        if (query == null) {
            return R.ok(new Page<>());
        }
        return R.ok(this.page(page, query));
    }

    /**
     * 获取异常记录列表
     *
     * @param errorRecord
     * @return
     */
    @Override
    public List<ErrorRecord> listErrorRecord(ErrorRecord errorRecord) {
        QueryWrapper<ErrorRecord> query = this.baseQuery(errorRecord);
        if (query == null) {
            return new ArrayList<>();
        }
        return this.list(query);
    }


    /**
     * 通过ID查询异常记录
     *
     * @param id
     * @return
     */
    @Override
    public ErrorRecord getErrorRecordById(Integer id) {
        return this.getById(id);
    }

    /**
     * 添加异常记录
     *
     * @param errorRecord
     * @return
     */
    @Override
    public R<Boolean> saveErrorRecord(ErrorRecord errorRecord) {
        SysUser user = SecurityUtils.getUser();
        errorRecord.setCreateBy(user.getId());
        log.info("添加异常记录！异常记录:{}", errorRecord);
        Boolean saveFlag = this.save(errorRecord);
        if (saveFlag) {
            return R.ok(Boolean.TRUE, "添加异常记录成功！");
        } else {
            return R.ok(Boolean.FALSE, "添加异常记录失败！");
        }
    }

    /**
     * 修改异常记录
     *
     * @param errorRecord
     * @return
     */
    @Override
    public R<Boolean> updateErrorRecord(ErrorRecord errorRecord) {
        SysUser user = SecurityUtils.getUser();
        errorRecord.setUpdateBy(user.getId());
        log.info("修改异常记录！id:{}", errorRecord.getId());
        UpdateWrapper<ErrorRecord> updateWrapper = new UpdateWrapper<ErrorRecord>();
        updateWrapper.lambda().set(ErrorRecord::getReadFlag, errorRecord.getReadFlag());
        updateWrapper.lambda().eq(ErrorRecord::getId, errorRecord.getId());
        Boolean updateFlag = this.update(errorRecord, updateWrapper);
        if (updateFlag) {
            return R.ok(Boolean.TRUE, "修改异常记录成功！");
        } else {
            return R.ok(Boolean.FALSE, "修改异常记录失败！");
        }

    }

    /**
     * 删除异常记录
     *
     * @param id
     * @return
     */
    @Override
    public R<Boolean> removeErrorRecordById(Integer id) {
        log.info("删除异常记录！id:{}", id);
        return R.ok(this.removeById(id), "删除异常记录成功！");
    }

    /**
     * 批量删除异常记录
     *
     * @param idList
     * @return
     */
    @Override
    public R<Boolean> batchDelete(List<Integer> idList) {
        log.info("批量删除异常记录！idList:{}", idList);
        return R.ok(this.removeByIds(idList), "批量删除异常记录成功！");
    }


    /**
     * 修改已读
     *
     * @param errorRecord
     * @return
     */
    @Override
    public R<Boolean> updateReadFlag(ErrorRecord errorRecord) {
        ErrorRecord updateErrorRecord = new ErrorRecord();
        updateErrorRecord.setId(errorRecord.getId());
        updateErrorRecord.setReadFlag(errorRecord.getReadFlag());
        boolean updateFlag = this.updateById(updateErrorRecord);
        if (updateFlag) {
            return R.ok(Boolean.TRUE, "修改已读成功！");
        } else {
            return R.ok(Boolean.FALSE, "修改已读失败！");
        }
    }

}