package com.xiaoke.model.kube.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.xiaoke.entity.system.entity.SysUser;
import com.xiaoke.utils.SecurityUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xiaoke.common.core.utils.R;
import cn.hutool.core.util.StrUtil;
import java.util.List;
import java.util.ArrayList;
import com.xiaoke.entity.kube.entity.AppPort;
import com.xiaoke.model.kube.mapper.AppPortMapper;
import com.xiaoke.model.kube.service.AppPortService;

import java.util.stream.Collectors;

/**
* 应用端口 Service
*
*  @author xiaoke
*  @date 2024-08-03 16:44:18
*/
@Service
@AllArgsConstructor
@Slf4j
public class AppPortServiceImpl extends ServiceImpl<AppPortMapper,AppPort> implements AppPortService{
    private final AppPortMapper appPortMapper;

    /**
     * 封装共用查询条件
     * @param appPort
     * @return
     */
    private QueryWrapper<AppPort> baseQuery(AppPort appPort){
        QueryWrapper<AppPort> query = Wrappers.query();
        if (appPort.getServiceId()!=null){
            query.lambda().eq(AppPort::getServiceId, appPort.getServiceId());
        }
        query.orderByDesc("id");
        return query;
    }


    /**
     * 分页查询应用端口
     * @param page
     * @param appPort
     * @return
     */
    @Override
    public R<IPage> pageAppPort(Page page, AppPort appPort){
        QueryWrapper<AppPort> query = this.baseQuery(appPort);
        if(query==null){
            return R.ok(new Page<>());
        }
        return R.ok(this.page(page, query));
    }

    /**
     * 获取应用端口列表
     * @param appPort
     * @return
     */
    @Override
    public List<AppPort> listAppPort(AppPort appPort){
        QueryWrapper<AppPort> query = this.baseQuery(appPort);
        if(query==null){
            return new ArrayList<>();
        }
        return this.list(query);
    }





    /**
     * 通过ID查询应用端口
     * @param id
     * @return
     */
    @Override
    public AppPort getAppPortById(Integer id){
        return this.getById(id);
    }

    /**
     * 添加应用端口
     * @param appPort
     * @return
     */
    @Override
    public R<Boolean> saveAppPort(AppPort appPort){
        log.info("添加应用端口！应用端口:{}",appPort);
        Boolean saveFlag = this.save(appPort);
        if(saveFlag){
            return R.ok(Boolean.TRUE,"添加应用端口成功！");
        }else{
            return R.ok(Boolean.FALSE,"添加应用端口失败！");
        }
    }

    /**
     * 修改应用端口
     * @param appPort
     * @return
     */
    @Override
    public R<Boolean> updateAppPort(AppPort appPort){
        log.info("修改应用端口！id:{}",appPort.getId());
        UpdateWrapper<AppPort> updateWrapper = new UpdateWrapper<AppPort>();
        updateWrapper.lambda().set(AppPort::getServiceId,appPort.getServiceId());
        updateWrapper.lambda().set(AppPort::getContainer,appPort.getContainer());
        updateWrapper.lambda().set(AppPort::getNode,appPort.getNode());
        updateWrapper.lambda().eq(AppPort::getId,appPort.getId());
        Boolean updateFlag = this.update(appPort,updateWrapper);
        if(updateFlag){
            return R.ok(Boolean.TRUE,"修改应用端口成功！");
        }else{
            return R.ok(Boolean.FALSE,"修改应用端口失败！");
        }

    }

    /**
     * 删除应用端口
     * @param id
     * @return
     */
    @Override
    public R<Boolean> removeAppPortById(Integer id){
        log.info("删除应用端口！id:{}",id);
        return R.ok(this.removeById(id),"删除应用端口成功！");
    }

}