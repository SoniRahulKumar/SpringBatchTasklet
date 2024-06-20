package com.springbatchtasklet.springbatchdemo.Scheduler;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class UploadDataIntoDB {

    @Autowired
    JobLauncher jobLauncher;
    @Autowired
    Job job;
    @Scheduled(fixedDelayString = "PT2M")
    //@Scheduled(fixedRate = 60000)
    public void uploadDataIntoDatabase() throws Exception {
        JobParameters params = new JobParametersBuilder()
                .addString("JobID", String.valueOf(System.currentTimeMillis()))
                .toJobParameters();

        jobLauncher.run(job, params);

    }


//    @Scheduled(cron = "0 0/2 * * *") // Example cron expression (runs every 2 hours)
//    public void runJob() throws Exception {
//        job.execute(null); // Trigger the chained job execution
//    }


}
