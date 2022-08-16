package com.example.springbatchtutorial.job.jobjobListener.JobLoggerListener;


import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;

@Slf4j
public class JobLoggerListener implements JobExecutionListener {

    private static String BEFORE_MESSAGE = "{} Job is Running ★★★★★★★★★★★★★★★★★★★★★★★";  // {} 해당 폼은 Job의 이름을 채워주기 위해 비워놓았다. 하단의 status 의 경우 Job의 상태.
    private static String AFTER_MESSAGE = "{} Job is Done. (status : {}) ★★★★★★★★★★★★★★★★★★★★★★★★";  //jobExecution.get 어쩌고<< 를 활용하여 상태까지 확인 할 수 있다.

    //리스너 전에 실행되는 Job
    @Override
    public void beforeJob(JobExecution jobExecution) {
        log.info(BEFORE_MESSAGE, jobExecution.getJobInstance().getJobName());
    }
    //리스너 후후에 실행되는 Job
   @Override
    public void afterJob(JobExecution jobExecution) {
        log.info(AFTER_MESSAGE, jobExecution.getJobInstance().getJobName(),
                                jobExecution.getStatus());

        if(jobExecution.getStatus() == BatchStatus.FAILED){
            // email 이나 메신저로 보낼수도있다.  지금은 단순히 로그만 확인하겠음.

            log.info("Job is failed ★★★★★★★★★★★★★★★★★★★★★★★★★");
        }
    }

    //이제 이렇게 만든것을 JobBuilder 로 돌아가서 .listener(new .class) 하여 사용할 수 있다.
}
