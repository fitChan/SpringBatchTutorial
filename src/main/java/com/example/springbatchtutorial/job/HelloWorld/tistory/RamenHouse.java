package com.example.springbatchtutorial.job.HelloWorld.tistory;


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

@Configuration
@RequiredArgsConstructor
public class RamenHouse {
    @Autowired
    private JobBuilderFactory jobBuilderFactory;
    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job chef(){
        return jobBuilderFactory.get("chef")
                .incrementer(new RunIdIncrementer())
                .listener(new ChefListener())
                .start(gimbob())
                .next(ramen())
                .next(icenoodle())
                .build();
    }

    @JobScope
    @Bean
    public Step icenoodle() {
        return stepBuilderFactory.get("icenoodle")
                .tasklet(makeIcenoodle())
                .build();
    }

    @StepScope
    @Bean
    public Tasklet makeIcenoodle() {
        return (contribution, chunkContext) -> {
            System.out.println("냉면이 완료 되었습니다.");
            return RepeatStatus.FINISHED;
        };
    }

    @JobScope
    @Bean
    public Step ramen() {
        return stepBuilderFactory.get("ramen")
                .tasklet(makeRamen())
                .build();
    }
    @StepScope
    @Bean
    public Tasklet makeRamen() {
        return (contribution, chunkContext) -> {
            System.out.println("라면이 완료 되었습니다.");
            return RepeatStatus.FINISHED;
        };
    }

    @JobScope
    @Bean
    public Step gimbob() {
        return stepBuilderFactory.get("gimbob")
                .tasklet(makeGimbob())
                .build();
    }
    @StepScope
    @Bean
    public Tasklet makeGimbob() {
        return (contribution, chunkContext) -> {
            System.out.println("김밥이 완료 되었습니다.");
            return RepeatStatus.FINISHED;
        };
    }

}
