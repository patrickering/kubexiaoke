package com.xiaoke.model.kube.controller;


import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.xiaoke.annotation.log.SysLog;
import com.xiaoke.annotation.permission.Permission;
import com.xiaoke.common.core.utils.R;
import com.xiaoke.entity.kube.entity.PushChannel;
import com.xiaoke.model.kube.service.PushChannelService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

/**
 * Controller
 * 推送渠道管理
 *
 * @author chendengwen
 * @date 2019-11-09 23:51:39
 */
@Api(value = "push-channel", tags = "推送渠道管理")
@RestController
@Slf4j
@AllArgsConstructor
@RequestMapping("${controller.prefix}/${controller.kube.prefix}/push-channel")
public class PushChannelController {
    private final PushChannelService pushChannelService;


    @ApiOperation("获取推送")
    @GetMapping("/{type}")
    @Permission("kube_push_view")
    public R<PushChannel> list(@PathVariable String type, PushChannel pushChannel) {
        PushChannel pushChannelQuery = new PushChannel();
        pushChannelQuery.setPushId(pushChannel.getPushId());
        pushChannel.setType(type);
        return R.ok(pushChannelService.getOne(Wrappers.query(pushChannel)));
    }


    /**
     * 修改推送渠道
     *
     * @param pushChannel 推送渠道信息
     * @return success/false
     */
    @PutMapping
    @SysLog("修改推送渠道")
    @ApiOperation("修改推送渠道")
    @Permission("kube_push_edit")
    @Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public R updateById(@RequestBody PushChannel pushChannel) {
        return R.ok(pushChannelService.updateById(pushChannel)).setMsg("修改成功！");
    }
}
