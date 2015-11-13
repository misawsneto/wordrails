package co.xarx.trix.jobs;

import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.quartz.JobExecutionContext;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

@Component
@Scope(value = BeanDefinition.SCOPE_PROTOTYPE)
public class SimpleJob extends QuartzJobBean {
    
 
    @Override
    protected void executeInternal(JobExecutionContext context) {        
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            System.out.println(MessageFormat.format("[{0}]{1}", formatter.format(context.getFireTime()), context.getScheduler().getSchedulerInstanceId()));
        } catch (SchedulerException ex) {
            Logger.getLogger(SimpleJob.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}