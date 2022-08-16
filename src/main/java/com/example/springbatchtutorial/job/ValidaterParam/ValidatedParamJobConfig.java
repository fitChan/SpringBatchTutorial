package com.example.springbatchtutorial.job.ValidaterParam;


import com.example.springbatchtutorial.job.ValidaterParam.Validator.FileParamValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.*;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.CompositeJobParametersValidator;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

/*
desc : 파일이름 파라미너 전달 그리고 검증
RUN : spring.batch.job.names = validatedParamJob  // fileName = test.csv
*/


@Configuration
@RequiredArgsConstructor
public class ValidatedParamJobConfig {

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;


    @Bean
    public Job validatedParamJob(Step validatedParamStep) {
        return jobBuilderFactory.get("validatedParamJob")
                .incrementer(new RunIdIncrementer())
                .start(validatedParamStep)
                .validator(validator())  //validator 는 다중으로 할수 있다. ex) CompositeJobParametersValidator 를 이용하여 validator를 배열로 설정하여 set할수있다.
                .build();
    }

    private CompositeJobParametersValidator validator() {
        CompositeJobParametersValidator validator = new CompositeJobParametersValidator();
        validator.setValidators(Arrays.asList(new FileParamValidator()));
        return validator;
    }

    @Bean
    @JobScope
    public Step validatedParamStep(Tasklet validatedParamTaskLet) {
        return stepBuilderFactory.get("validatedParamStep")
                .tasklet(validatedParamTaskLet)
                .build();
    }

    @StepScope
    @Bean
    public Tasklet validatedParamTaskLet(@Value("#{jobParameters['fileName']}") String fileName) {
        return (contribution, chunkContext) -> {
            System.out.println(fileName);
            System.out.println("validated Param Spring Batch");
            return RepeatStatus.FINISHED;
        };
    }

}
