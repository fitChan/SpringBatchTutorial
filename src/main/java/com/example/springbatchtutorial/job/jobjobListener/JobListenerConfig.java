package com.example.springbatchtutorial.job.jobjobListener;


import com.example.springbatchtutorial.job.jobjobListener.JobLoggerListener.JobLoggerListener;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/*
desc : 파일이름 파라미너 전달 그리고 검증
RUN : spring.batch.job.names=jobListenerJob
*/


@Configuration
@RequiredArgsConstructor
public class JobListenerConfig {

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;


    @Bean
    public Job jobListenerJob(Step jobListenerStep) {
        return jobBuilderFactory.get("jobListenerJob")
                .incrementer(new RunIdIncrementer())
                .start(jobListenerStep)
                .listener(new JobLoggerListener())
                .build();
    }
    @Bean
    @JobScope
    public Step jobListenerStep(Tasklet jobListenerTasklet) {
        return stepBuilderFactory.get("jobListenerStep")
                .tasklet(jobListenerTasklet)
                .build();
    }

    @StepScope
    @Bean
    public Tasklet jobListenerTasklet() {
        return (contribution, chunkContext) -> {

            System.out.println("Job Listener Tasklet");
            return RepeatStatus.FINISHED;
//            throw new Exception("failed!!!!!!!!!!!!!!"); 오류 낼때 이렇게 처리 할 수 있다
        };
    }

}
