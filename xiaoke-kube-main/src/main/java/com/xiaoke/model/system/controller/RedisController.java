package com.xiaoke.model.system.controller;

import com.xiaoke.common.core.utils.R;
import com.xiaoke.model.system.service.RedisService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author xiaoke
 * @date 2019-05-08
 * <p>
 * redis 数据
 */
@RestController
@AllArgsConstructor
@RequestMapping("${controller.prefix}/${controller.system.prefix}/redis")
public class RedisController {

    private final RedisService redisService;

    /**
     * 查询redis信息
     *
     * @return
     */
    @GetMapping("/info")
    public R memory() {
        return R.ok(redisService.getInfo());
    }

}
