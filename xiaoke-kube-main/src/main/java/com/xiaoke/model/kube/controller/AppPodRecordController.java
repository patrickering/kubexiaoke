package com.xiaoke.model.kube.controller;

import com.xiaoke.annotation.validate.ValidateFiled;
import com.xiaoke.annotation.validate.ValidateGroup;
import com.xiaoke.common.core.constant.Constant;
import com.xiaoke.entity.kube.dto.AppPodRecordDTO;
import io.swagger.annotations.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.xiaoke.annotation.permission.Permission;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xiaoke.common.core.utils.R;
import com.xiaoke.annotation.log.SysLog;
import com.xiaoke.entity.kube.entity.AppPodRecord;
import com.xiaoke.model.kube.service.AppPodRecordService;
/**
* 应用pod记录 Controller
*
*  @author xiaoke
*  @date 2024-08-14 23:23:56
*/
@Api(value = "appPodRecord", tags = "应用pod记录")
@RestController
@Slf4j
@AllArgsConstructor
@RequestMapping("${controller.prefix}/${controller.kube.prefix}/app-pod-record")
public class AppPodRecordController {

    private final AppPodRecordService appPodRecordService;

	@ApiOperation("获取应用pod记录列表")
    @GetMapping("/list")
    @Permission("kube_app_view")
    public R list(AppPodRecord appPodRecord) {
        return R.ok(appPodRecordService.listAppPodRecord(appPodRecord));
    }

    @ApiOperation("获取pod日志")
    @GetMapping("pod-history-log")
    @Permission("kube_app_view")
    public R<IPage> podHistoryLog(Page page, AppPodRecordDTO appPodRecord, @RequestHeader(Constant.HEADER_NAME_SPACE_KEY) String namespace) {
        return appPodRecordService.podHistoryLog(page,appPodRecord,namespace);
    }
}