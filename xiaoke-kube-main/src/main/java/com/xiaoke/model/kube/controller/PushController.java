package com.xiaoke.model.kube.controller;


import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.xiaoke.annotation.log.SysLog;
import com.xiaoke.annotation.permission.Permission;
import com.xiaoke.common.core.utils.R;
import com.xiaoke.entity.kube.entity.Push;
import com.xiaoke.model.kube.service.PushService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller
 * 推送
 *
 * @author chendengwen
 * @date 2019-11-09 23:51:39
 */
@Api(value = "push", tags = "推送")
@RestController
@Slf4j
@AllArgsConstructor
@RequestMapping("${controller.prefix}/${controller.kube.prefix}/push")
public class PushController {
    private final PushService pushService;


    @ApiOperation("获取推送")
    @GetMapping("/list")
    @Permission("kube_push_view")
    public R<List<Push>> list(Push push) {
        List<Push> list = pushService.list(Wrappers.query(push).orderByAsc("id"));
        return R.ok(list);
    }

    @ApiOperation("获取推送")
    @GetMapping("/{type}")
    @Permission("kube_push_view")
    public R<Push> list(@PathVariable String type) {
        Push push = new Push();
        push.setType(type);
        return R.ok(pushService.getOne(Wrappers.query(push)));
    }

    /**
     * 修改推送
     *
     * @param push 修改信息
     * @return success/false
     */
    @PutMapping
    @SysLog("修改推送")
    @ApiOperation("修改推送")
    @Permission("kube_push_edit")
    @Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public R updateById(@RequestBody Push push) {
        return R.ok(pushService.updateById(push)).setMsg("修改成功！");
    }

}
