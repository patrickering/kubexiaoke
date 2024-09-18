package com.xiaoke.job;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@EnableScheduling
@AllArgsConstructor
public class PodJobHandler {
    private final PodTaskService podTaskService;

    @Scheduled(cron = "0/20 * * * * ?")
    public void execute() {
        log.info("=============>定时获取pod");
        try {
            podTaskService.runJobTask();
        } catch (Exception e) {
            log.error("调度异常:{}", e.getMessage());
        }
    }

}
