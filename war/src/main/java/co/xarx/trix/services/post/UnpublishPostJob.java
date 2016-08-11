package co.xarx.trix.services.post;

import co.xarx.trix.config.multitenancy.TenantContextHolder;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Service;

@Service
public class UnpublishPostJob extends QuartzJobBean implements InterruptableJob, Job {
    @Autowired
    private Scheduler scheduler;
    @Autowired
    private PostService postService;
    private JobKey jobKey;

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        this.jobKey = context.getJobDetail().getKey();
        JobDataMap dataMap = context.getJobDetail().getJobDataMap();
        String tenantId = dataMap.getString("tenantId");
        TenantContextHolder.setCurrentTenantId(tenantId);

        Integer postId = Integer.valueOf(dataMap.getString("postId"));

        postService.unpublishPost(postId);
    }

    @Override
    public void interrupt() throws UnableToInterruptJobException {
        scheduler.interrupt(jobKey);
    }

}
