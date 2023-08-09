package cc.jooylife.meerkat.collector.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

import java.util.concurrent.ThreadPoolExecutor;

@Configuration
public class ScheduleConfig implements SchedulingConfigurer {

    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        ThreadPoolTaskScheduler executor = new ThreadPoolTaskScheduler();
        int cpuNum = Runtime.getRuntime().availableProcessors();
        executor.setPoolSize(2*cpuNum+1);
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.DiscardPolicy());
        executor.setThreadNamePrefix("task-executor-");
        executor.initialize();
        taskRegistrar.setScheduler(executor);
    }
}
