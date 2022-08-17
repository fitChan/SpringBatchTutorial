package com.example.springbatchtutorial.job.HelloWorld.tistory2;


import com.example.springbatchtutorial.job.HelloWorld.tistory2.Entity.Chef;
import com.example.springbatchtutorial.job.HelloWorld.tistory2.Entity.ChefCareer;
import com.example.springbatchtutorial.job.HelloWorld.tistory2.repository.ChefCareerRepository;
import com.example.springbatchtutorial.job.HelloWorld.tistory2.repository.ChefRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.data.RepositoryItemReader;
import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.batch.item.data.builder.RepositoryItemReaderBuilder;
import org.springframework.batch.item.data.builder.RepositoryItemWriterBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Sort;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Configuration
@RequiredArgsConstructor
public class chefConfig {
    @Autowired
    private JobBuilderFactory jobBuilderFactory;
    @Autowired
    private StepBuilderFactory stepBuilderFactory;
    @Autowired
    private ChefRepository chefRepository;
    @Autowired
    private ChefCareerRepository chefCareerRepository;

    @Bean
    public Job chefJob(Step chefStep) {
        return jobBuilderFactory.get("chefJob")
                .incrementer(new RunIdIncrementer())
                .start(chefStep)
                .build();
    }

    @JobScope
    @Bean
    public Step chefStep(ItemReader chefReader,
                         ItemProcessor chefProcessor,
                         ItemWriter chefWriter) {
        return stepBuilderFactory.get("chefStep")
                .<Chef, ChefCareer>chunk(5)
                .reader(chefReader)
                .processor(chefProcessor)
                .writer(chefWriter)
                .build();
    }

    @Bean
    @StepScope
    public RepositoryItemReader<Chef> chefReader() {
        return new RepositoryItemReaderBuilder<Chef>()
                .name("chefReader")
                .repository(chefRepository)
                .methodName("findAll")
                .pageSize(5)
                .arguments(Arrays.asList())
                .sorts(Collections.singletonMap("id", Sort.Direction.ASC))
                .build();

    }

    @Bean
    @StepScope
    public ItemProcessor<Chef, ChefCareer> chefProcessor(){
        return ChefCareer::new;
    }

    @Bean
    @StepScope
    public ItemWriter<ChefCareer> chefWriter(){
        return new ItemWriter<ChefCareer>() {
            @Override
            public void write(List<? extends ChefCareer> items) throws Exception {
                items.forEach(e -> chefCareerRepository.save(e));
            }
        };
    }
}
