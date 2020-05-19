package com.zust.yan.rpc.monitor.app.listener;

import com.zust.yan.rpc.common.utils.RpcUtils;
import com.zust.yan.rpc.register.service.RegisterService;
import com.zust.yan.rpc.spring.factory.RegisterServiceFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;

/**
 * @author yan
 */
@Component
@Slf4j
public class ServiceDataSyncListener implements ApplicationListener<ContextRefreshedEvent> {
    private final static String TIME_CRON = "0/5 * * * * ? ";
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        RpcUtils.init();
        RegisterService registerService = RegisterServiceFactory.getRegisterService();
        registerService.sync(true);
        ThreadPoolTaskScheduler taskExecutor = serverTaskExecutor();
        taskExecutor.schedule(() -> {
            log.info("sync  service");
            registerService.sync(true);
        }, new CronTrigger(TIME_CRON));
    }

    public ThreadPoolTaskScheduler serverTaskExecutor() {
        ThreadPoolTaskScheduler taskExecutor = new ThreadPoolTaskScheduler();
        taskExecutor.setPoolSize(Runtime.getRuntime().availableProcessors() * 2);
        taskExecutor.setThreadNamePrefix("ServerTask--");
        taskExecutor.setWaitForTasksToCompleteOnShutdown(false);
        taskExecutor.setAwaitTerminationSeconds(5);
        taskExecutor.initialize();
        return taskExecutor;
    }
}
