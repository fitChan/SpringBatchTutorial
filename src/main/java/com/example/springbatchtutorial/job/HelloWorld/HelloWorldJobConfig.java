package com.example.springbatchtutorial.job.HelloWorld;


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
desc : Hello World Spring Batch 를 출력하는 JOB - STEP
RUN : spring.batch.job.names = helloWorldJob
*/


@Configuration
@RequiredArgsConstructor
public class HelloWorldJobConfig{


    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job helloWorldJob(){
        return jobBuilderFactory.get("helloworldJob")
                .incrementer(new RunIdIncrementer())
                .start(helloWorldStep())
                .build();
    }


    @Bean
    @JobScope
    public Step helloWorldStep() {
        return stepBuilderFactory.get("helloworldStep")
                .tasklet(helloWorldTaskLet())
                .build();
    }

    @StepScope
    @Bean
    public Tasklet helloWorldTaskLet() {
        return (contribution, chunkContext) -> {
            System.out.println("Hello World Spring Batch");
            return RepeatStatus.FINISHED;
        };
    }


}
