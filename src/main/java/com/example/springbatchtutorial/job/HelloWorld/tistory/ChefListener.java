package com.example.springbatchtutorial.job.HelloWorld.tistory;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;

@Slf4j
public class ChefListener implements JobExecutionListener {


    private static String BEFORE = "{} Chef will start her Job. ★★★★★★★★★★★★★★★★★★★★★★★";
    private static String AFTER = "{} chef is done her Job (status : {}) ★★★★★★★★★★★★★★★★★★★★★★★★";

    @Override
    public void beforeJob(JobExecution jobExecution) {
        log.info(BEFORE, jobExecution.getJobInstance().getJobName());
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        log.info(AFTER, jobExecution.getJobInstance().getJobName(),
                        jobExecution.getStatus());

        if(jobExecution.getStatus() == BatchStatus.FAILED){
            log.info("Chef is fired ★★★★★★★★★★★★★★★★★★★★★★★★");
        }
    }
}
