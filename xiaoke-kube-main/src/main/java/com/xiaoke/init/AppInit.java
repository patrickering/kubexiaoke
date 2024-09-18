package com.xiaoke.init;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * spring Boot 启动运行
 *
 * @author xiaoke
 */
@Component
@Slf4j
public class AppInit implements ApplicationRunner {

    /**
     * 应用名称
     */
    @Value("${app.name}")
    private String appName;

    /**
     * 应用地址
     */
    @Value("${app.url}")
    private String appUrl;

    @Override
    public void run(ApplicationArguments var1){
        try {
            StringBuilder sb = new StringBuilder();
            sb.append("\r\n======================================================================\r\n");
            sb.append("\r\n    欢迎使用 " + appName + "  - Powered By " + appUrl + "\r\n");
            sb.append("\r\n======================================================================\r\n");
            System.out.println(sb.toString());
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }
}
