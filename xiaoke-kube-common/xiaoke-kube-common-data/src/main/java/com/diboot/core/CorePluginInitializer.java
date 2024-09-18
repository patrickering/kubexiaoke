package com.diboot.core;

import com.diboot.core.util.SqlFileInitializer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

/**
 * 组件初始化
 * @author mazc@dibo.ltd
 * @version v2.0
 * @date 2020/11/28
 */
@Slf4j
@Component
@Order(910)
@ComponentScan("com.diboot.core")
public class CorePluginInitializer implements ApplicationRunner {
    @Autowired
    private Environment environment;

    @Override
    public void run(ApplicationArguments args) {
        // 初始化SCHEMA
        SqlFileInitializer.init(environment);

    }
}
