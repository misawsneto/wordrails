package co.xarx.trix.learning;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.Scheduler;
import org.springframework.beans.factory.annotation.Autowired;

public class SchedulerLearning {

	@Autowired
	public Scheduler scheduler;

	public class StatsJob implements Job {

		@Override
		public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {

		}

	}

	public static void mains(String[] args){


	}
}
