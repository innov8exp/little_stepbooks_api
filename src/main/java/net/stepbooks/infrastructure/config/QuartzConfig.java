package net.stepbooks.infrastructure.config;

import net.stepbooks.domain.order.job.OrderJob;
import org.quartz.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class QuartzConfig {

    @Bean
    public JobDetail orderJobDetail() {
        return JobBuilder.newJob(OrderJob.class)
                .withIdentity("OrderJob")
                .withDescription("Invoke Order Job service...")
                .storeDurably()
                .build();
    }

    @Bean
    public Trigger orderJobTrigger(JobDetail orderJobDetail) {
        return TriggerBuilder.newTrigger().forJob(orderJobDetail)
                .withIdentity("OrderJobTrigger")
                .withDescription("Order Job Trigger")
                .withSchedule(CronScheduleBuilder.cronSchedule("0 0/30 * * * ?"))
                .startNow()
                .build();
    }

}
