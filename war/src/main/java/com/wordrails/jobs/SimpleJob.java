package com.wordrails.jobs;

import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

@Component
@Scope(value = BeanDefinition.SCOPE_PROTOTYPE)
public class SimpleJob extends QuartzJobBean {
    
 
    @Override
    protected void executeInternal(JobExecutionContext context) {
        System.out.println("THIS IS A QUARTZ TEST");
    }
}