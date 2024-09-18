

package com.xiaoke.model.system.controller;


import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xiaoke.annotation.permission.Permission;
import com.xiaoke.common.core.utils.R;
import com.xiaoke.entity.system.entity.SysLog;
import com.xiaoke.entity.system.vo.PreLogVo;
import com.xiaoke.model.system.service.LogService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * <p>
 * 日志表 前端控制器
 * </p>
 *
 * @author xiaoke
 * @since 2017-11-20
 */
@RestController
@AllArgsConstructor
@RequestMapping("${controller.prefix}/${controller.system.prefix}/log")
@Api(value = "log", tags = "日志管理模块")
public class LogController {
    private final LogService logService;

    /**
     * 分页查询
     *
     * @param page   分页对象
     * @param sysLog 系统日志
     * @return
     */
    @ApiOperation("分页查询日志")
    @GetMapping("/page")
    public R getLogPage(Page page, SysLog sysLog) {
        return R.ok(logService.page(page, Wrappers.query(sysLog)));
    }

    /**
     * 删除日志
     *
     * @param id ID
     * @return success/false
     */
    @ApiOperation("删除日志")
    @DeleteMapping("/{id}")
    @Permission("sys_log_del")
    public R removeById(@PathVariable Long id) {
        return R.ok(logService.removeById(id));
    }

    /**
     * 插入日志
     *
     * @param sysLog 日志实体
     * @return success/false
     */
    @ApiOperation("添加日志")
    @PostMapping("/save")
    public R save(@Valid @RequestBody SysLog sysLog) {
        return R.ok(logService.save(sysLog));
    }

    /**
     * 批量插入前端异常日志
     *
     * @param preLogVoList 日志实体
     * @return success/false
     */
    @ApiOperation("批量插入前端异常日志")
    @PostMapping("/logs")
    public R saveBatchLogs(@RequestBody List<PreLogVo> preLogVoList) {
        return R.ok(logService.saveBatchLogs(preLogVoList));
    }
}
