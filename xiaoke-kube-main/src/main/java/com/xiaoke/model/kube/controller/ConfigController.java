package com.xiaoke.model.kube.controller;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xiaoke.annotation.idempotent.Idempotent;
import com.xiaoke.annotation.log.SysLog;
import com.xiaoke.annotation.permission.Permission;
import com.xiaoke.annotation.validate.ValidateFiled;
import com.xiaoke.annotation.validate.ValidateGroup;
import com.xiaoke.common.core.constant.Constant;
import com.xiaoke.common.core.utils.R;
import com.xiaoke.entity.kube.dto.ConfigDTO;
import com.xiaoke.entity.kube.entity.Config;
import com.xiaoke.model.kube.service.ConfigService;
import io.swagger.annotations.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 配置 Controller
 *
 * @author xiaoke
 * @date 2024-08-03 16:10:50
 */
@Api(value = "config", tags = "配置")
@RestController
@Slf4j
@AllArgsConstructor
@RequestMapping("${controller.prefix}/${controller.kube.prefix}/config")
public class ConfigController {

    private final ConfigService configService;


    @ApiOperation("分页查询配置")
    @ApiImplicitParams({@ApiImplicitParam(name = "size", value = "每页条数", defaultValue = "10", dataType = "int", paramType = "query", required = true), @ApiImplicitParam(name = "current", value = "页数", defaultValue = "1", dataType = "int", paramType = "query", required = true)})
    @GetMapping("/page")
    @ValidateGroup(fileds = {@ValidateFiled(index = 0, filedName = "current", notNull = true, msg = "缺少current参数！"), @ValidateFiled(index = 0, filedName = "size", notNull = true, msg = "缺少size参数")})
    @Permission("kube_config_view")
    public R<IPage> page(Page page, Config config, @RequestHeader(Constant.HEADER_NAME_SPACE_KEY) String namespace) {
        config.setNamespace(namespace);
        return configService.pageConfig(page, config);
    }


    @ApiOperation("获取配置列表")
    @GetMapping("/list")
    @Permission("kube_config_view")
    public R list(Config config, @RequestHeader(Constant.HEADER_NAME_SPACE_KEY) String namespace) {
        config.setNamespace(namespace);
        return R.ok(configService.listConfig(config));
    }


    @ApiOperation("通过ID查询配置")
    @GetMapping("/get-by-id/{id}")
    @Permission("kube_config_view")
    public R getById(@ApiParam(name = "id", value = "配置ID", required = true) @PathVariable Integer id) {
        return R.ok(configService.getConfigById(id));
    }


    @ApiOperation("添加配置")
    @SysLog("添加配置")
    @PostMapping
    @ValidateGroup(fileds = {@ValidateFiled(index = 0, notNull = true, maxLen = 100, filedName = "name", msg = "[名称]验证未通过！"), @ValidateFiled(index = 0, maxLen = 10, filedName = "type", msg = "[类型]验证未通过！"), @ValidateFiled(index = 0, maxLen = 255, filedName = "remark", msg = "[描述]验证未通过！"),})
    @Permission("kube_config_add")
    @Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public R<Boolean> save(@RequestBody ConfigDTO config, @RequestHeader(Constant.HEADER_NAME_SPACE_KEY) String namespace) throws Exception {
        return configService.saveConfig(config, namespace);
    }


    @ApiOperation("修改配置")
    @SysLog("修改配置")
    @PutMapping
    @ValidateGroup(fileds = {@ValidateFiled(index = 0, notNull = true, filedName = "id", msg = "缺少id参数！"), @ValidateFiled(index = 0, maxLen = 255, filedName = "remark", msg = "[描述]验证未通过！")})
    @Permission("kube_config_edit")
    @Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public R<Boolean> update(@RequestBody Config config) {
        return configService.updateConfig(config);
    }


    @ApiOperation("删除配置")
    @SysLog("删除配置")
    @DeleteMapping("/{id}")
    @ValidateGroup(fileds = {@ValidateFiled(index = 0, notNull = true, msg = "缺少id参数！")})
    @Permission("kube_config_del")
    @Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public R<Boolean> removeById(@PathVariable Integer id) {
        return configService.removeConfigById(id);
    }


    /**
     * 上传配置文件读取文件内容
     *
     * @param file
     * @return
     */
    @ApiOperation(value = "上传配置文件读取文件内容", notes = "上传配置文件读取文件内容")
    @PostMapping("/upload")
    @Idempotent(value = false)
    public R upload(@RequestParam("file") MultipartFile file) throws Exception {
        String content = new String(file.getBytes());

        String fileName = file.getOriginalFilename();
        String ext = fileName.substring(fileName.lastIndexOf(".") + 1);
        if ("pem".equals(ext) || "crt".equals(ext)) {
            try {
                CertificateFactory cf = CertificateFactory.getInstance("X.509");
                X509Certificate cert = (X509Certificate) cf.generateCertificate(file.getInputStream());

                Map<String, Object> returnMap = new HashMap<>();
                returnMap.put("content", content);
                //证书版本
                returnMap.put("version", cert.getVersion());
                //证书序列号
                returnMap.put("serial", cert.getSerialNumber().toString(16));
                //证书签发日期
                Date issue = cert.getNotBefore();
                returnMap.put("issue", DateUtil.format(issue, "yyyy-MM-dd HH:mm:ss"));
                //证书到期日期
                Date expire = cert.getNotAfter();
                returnMap.put("expire", DateUtil.format(expire, "yyyy-MM-dd HH:mm:ss"));
                //域名
                returnMap.put("domain", cert.getSubjectDN().getName());
                //颁发机构
                returnMap.put("issuer", cert.getIssuerDN().getName());
                //签名算法
                returnMap.put("algorithm", cert.getSigAlgName());
                return R.ok(returnMap);
            } catch (Exception e) {
                return R.failed("解析SSL证书失败！");
            }
        }
        return R.ok(content);
    }


}