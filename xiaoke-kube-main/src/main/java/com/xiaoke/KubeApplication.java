package com.xiaoke;

import com.xiaoke.annotation.swagger.EnableSwagger2;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * springBoot 启动入口
 *
 * @author xiaoke
 */
@EnableTransactionManagement
@SpringBootApplication
@EnableSwagger2
@Configuration
@EnableAsync
@Import(cn.hutool.extra.spring.SpringUtil.class)
public class KubeApplication {
    public static ConfigurableApplicationContext context;

    /**
     * 应用程序的主入口点
     * 通过Spring Boot启动应用程序
     *
     * @param args 命令行参数，用于启动应用程序
     */
    public static void main(String[] args) {
        context = SpringApplication.run(KubeApplication.class, args);
    }

    /**
     * 配置并返回一个ObjectMapper实例
     * 该实例用于JSON处理，定制了序列化行为，仅包含非空值字段
     * 这有助于提高数据传输效率，并减少不必要的数据传输
     *
     * @return 配置好的ObjectMapper实例
     */
    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper().setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }

    @Bean
    public TaskScheduler taskScheduler() {
        ThreadPoolTaskScheduler taskScheduler = new ThreadPoolTaskScheduler();
        taskScheduler.setPoolSize(10);
        taskScheduler.initialize();
        return taskScheduler;
    }
}
