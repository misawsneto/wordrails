package com.wordrails.job;

import com.wordrails.test.AbstractTest;
import java.util.Date;
import static org.hamcrest.CoreMatchers.is;
import org.joda.time.DateMidnight;
import org.joda.time.DateTime;
import static org.junit.Assert.assertThat;
import org.junit.Test;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

public class QuartzCronTriggerTest extends AbstractTest {
 
    @Autowired
    private SchedulerFactoryBean quartzScheduler;
 
    @Test
    public void testFirstTrigger() throws SchedulerException {
        Trigger firstTrigger = quartzScheduler.getScheduler().getTrigger(new TriggerKey("firstTrigger"));
 
        //Must use tomorrow for testing because jobs have startTime of now
        DateTime tomorrow = new DateMidnight().toDateTime().plusDays(1);
 
        //Test first
        Date next = firstTrigger.getFireTimeAfter(tomorrow.toDate());
        DateTime expected = tomorrow.plusHours(5);
        assertThat(next, is(expected.toDate()));
 
        //Test the next day
        next = firstTrigger.getFireTimeAfter(next);
        expected = expected.plusDays(1);
        assertThat(next, is(expected.toDate()));
    }
}