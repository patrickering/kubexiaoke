package com.xiaoke.model.system.controller;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.map.MapUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xiaoke.annotation.permission.Permission;
import com.xiaoke.common.core.constant.CacheConstants;
import com.xiaoke.common.core.constant.PaginationConstants;
import com.xiaoke.common.core.utils.R;
import com.xiaoke.entity.system.entity.SysUser;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.ConvertingCursor;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Api(value = "Token相关API", tags = {"Token接口"})
@RestController
@Slf4j
@AllArgsConstructor
@RequestMapping("${controller.prefix}/${controller.system.prefix}/token")
public class TokenController {

    private final RedisTemplate redisTemplate;

    @ApiOperation("分页查询token")
    @PostMapping("/page")
    @Permission("token_token_view")
    public R<Page> tokenList(@RequestBody Map<String, Object> params) {
        //根据分页参数获取对应数据
        String key = String.format("%s*", CacheConstants.USER_DETAILS);
        List<String> pages = findKeysForPage(key, MapUtil.getInt(params, PaginationConstants.CURRENT)
                , MapUtil.getInt(params, PaginationConstants.SIZE));

        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new JdkSerializationRedisSerializer());
        Page result = new Page(MapUtil.getInt(params, PaginationConstants.CURRENT), MapUtil.getInt(params, PaginationConstants.SIZE));

        List<Map<String, Object>> returnList = new ArrayList<>();
        for (String page : pages) {
            SysUser sysUser = (SysUser) redisTemplate.opsForValue().get(page);
            Map<String, Object> map = new HashMap<>();
            Long expire = redisTemplate.opsForValue().getOperations().getExpire(page);
            map.put("token", page);
            map.put("expire", expire);
            map.put("name", sysUser.getName());
            map.put("username", sysUser.getUsername());
            map.put("email", sysUser.getEmail());
            map.put("phone", sysUser.getPhone());
            map.put("loginIp", sysUser.getLoginIp());
            map.put("loginDate", DateUtil.format(sysUser.getLoginDate(), "yyyy-MM-dd HH:mm:ss"));
            map.put("role", sysUser.getRole());
            returnList.add(map);
        }
        result.setRecords(returnList);
        result.setTotal(redisTemplate.keys(key).size());
        return R.ok(result);
    }


    @ApiOperation("删除token")
    @DeleteMapping("/{token}")
    @Permission("token_token_del")
    public R<Boolean> delete(@PathVariable String token) {
        redisTemplate.delete(token);
        return R.ok(Boolean.TRUE).setMsg("删除成功！");
    }


    private List<String> findKeysForPage(String patternKey, int pageNum, int pageSize) {
        ScanOptions options = ScanOptions.scanOptions().count(1000L)
                .match(patternKey).build();
        RedisSerializer<String> redisSerializer = (RedisSerializer<String>) redisTemplate.getKeySerializer();
        Cursor cursor = (Cursor) redisTemplate.executeWithStickyConnection(redisConnection -> new ConvertingCursor<>(redisConnection.scan(options), redisSerializer::deserialize));
        List<String> result = new ArrayList<>();
        int tmpIndex = 0;
        int startIndex = (pageNum - 1) * pageSize;
        int end = pageNum * pageSize;

        assert cursor != null;
        while (cursor.hasNext()) {
            if (tmpIndex >= startIndex && tmpIndex < end) {
                result.add(cursor.next().toString());
                tmpIndex++;
                continue;
            }
            if (tmpIndex >= end) {
                break;
            }
            tmpIndex++;
            cursor.next();
        }

        try {
            cursor.close();
        } catch (IOException e) {
            log.error("关闭cursor 失败");
        }
        return result;
    }

}
