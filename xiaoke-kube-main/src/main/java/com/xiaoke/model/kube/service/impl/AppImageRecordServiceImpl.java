package com.xiaoke.model.kube.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xiaoke.common.core.utils.R;
import com.xiaoke.entity.kube.entity.AppImageRecord;
import com.xiaoke.entity.kube.vo.AppImageRecordVO;
import com.xiaoke.entity.kube.vo.AppVO;
import com.xiaoke.model.kube.mapper.AppImageRecordMapper;
import com.xiaoke.model.kube.service.AppImageRecordService;
import com.xiaoke.model.kube.service.AppService;
import com.xiaoke.utils.JoinUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 应用镜像更新记录 Service
 *
 * @author xiaoke
 * @date 2024-08-14 00:03:31
 */
@Service
@AllArgsConstructor
@Slf4j
public class AppImageRecordServiceImpl extends ServiceImpl<AppImageRecordMapper, AppImageRecord> implements AppImageRecordService {
    private final AppImageRecordMapper appImageRecordMapper;
    private final AppService appService;

    /**
     * 封装共用查询条件
     *
     * @param appImageRecord
     * @return
     */
    private QueryWrapper<AppImageRecord> baseQuery(AppImageRecord appImageRecord) {
        QueryWrapper<AppImageRecord> query = Wrappers.query();
        if (appImageRecord.getAppId() != null) {
            query.lambda().eq(AppImageRecord::getAppId, appImageRecord.getAppId());
        }
        query.orderByDesc("id");
        return query;
    }


    /**
     * 分页查询应用镜像更新记录
     *
     * @param page
     * @param appImageRecord
     * @return
     */
    @Override
    public R<IPage> pageAppImageRecord(Page page, AppImageRecord appImageRecord) {
        QueryWrapper<AppImageRecord> query = this.baseQuery(appImageRecord);
        if (query == null) {
            return R.ok(new Page<>());
        }
        return R.ok(JoinUtil.page(this.page(page, query), AppImageRecordVO.class));
    }

    /**
     * 获取应用镜像更新记录列表
     *
     * @param appImageRecord
     * @return
     */
    @Override
    public List<AppImageRecordVO> listAppImageRecord(AppImageRecord appImageRecord) {
        QueryWrapper<AppImageRecord> query = this.baseQuery(appImageRecord);
        if (query == null) {
            return new ArrayList<>();
        }
        return JoinUtil.list(this.list(query), AppImageRecordVO.class);
    }


    /**
     * 通过ID查询应用镜像更新记录
     *
     * @param id
     * @return
     */
    @Override
    public AppImageRecordVO getAppImageRecordById(Integer id) {
        return JoinUtil.entity(this.getById(id), AppImageRecordVO.class);
    }

    /**
     * 回滚镜像
     *
     * @param id
     * @return
     * @throws Exception
     */
    @Override
    public R<Boolean> goBack(Integer id) throws Exception {
        AppImageRecord appImageRecord = this.getById(id);
        if (appImageRecord == null) {
            return R.failed("镜像不存在！");
        }
        AppVO app = appService.getAppById(appImageRecord.getAppId());
        if (app == null) {
            return R.failed("未查询到应用信息！");
        }
        app.setImage(appImageRecord.getImage());
        app.setImageVersion(appImageRecord.getImageVersion());
        appService.updateImage(app);
        return R.ok(Boolean.TRUE, "回滚成功！");
    }


}