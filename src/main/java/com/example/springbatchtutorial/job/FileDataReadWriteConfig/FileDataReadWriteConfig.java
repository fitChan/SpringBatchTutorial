package com.example.springbatchtutorial.job.FileDataReadWriteConfig;

import com.example.springbatchtutorial.job.FileDataReadWriteConfig.entity.Player;
import com.example.springbatchtutorial.job.FileDataReadWriteConfig.entity.PlayerYears;
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
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.builder.FlatFileItemWriterBuilder;
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;


/*
desc : Players.cvs 파일에 있는 정보를 순차적으로 읽어 쓰는 작업
RUN : spring.batch.job.names=fileReadWriteJob
*/

@RequiredArgsConstructor
@Configuration
public class FileDataReadWriteConfig {

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job fileReadWriteJob(Step fileReadWriteStep){
        return jobBuilderFactory.get("fileReadWriteJob")
                .incrementer(new RunIdIncrementer())
                .start(fileReadWriteStep)
                .build();
    }

    @Bean
    @JobScope
    public Step fileReadWriteStep(ItemReader playerFlatFileItemReader,
                                  ItemWriter playerItemWriter,
                                  ItemProcessor playerItemProcessor){
        return stepBuilderFactory.get("fileReadWriteStep")
                .<Player, Player>chunk(5)
                .reader(playerFlatFileItemReader)
//                .writer(items -> items.forEach(System.out::println))
                .processor(playerItemProcessor)
                .writer(playerItemWriter)
                .build();
    }

    @StepScope
    @Bean
    public FlatFileItemWriter<PlayerYears> playerItemWriter(){
        BeanWrapperFieldExtractor<PlayerYears> fieldExtractor = new BeanWrapperFieldExtractor<>();
        fieldExtractor.setNames(new String[]{"ID", "lastName", "position", "yearsExperience"});
        fieldExtractor.afterPropertiesSet();

        DelimitedLineAggregator<PlayerYears> lineAggregator = new DelimitedLineAggregator<>();
        lineAggregator.setDelimiter(",");
        lineAggregator.setFieldExtractor(fieldExtractor);

        FileSystemResource outputResource = new FileSystemResource("player_output.txt");

        return new FlatFileItemWriterBuilder<PlayerYears>()
                .name("playerItemWriter")
                .resource(outputResource)
                .lineAggregator(lineAggregator)
                .build();


    }


    @Bean
    @StepScope
    public ItemProcessor<Player, PlayerYears> playerItemProcessor(){
        return new ItemProcessor<Player, PlayerYears>() {
            @Override
            public PlayerYears process(Player item) throws Exception {
                return new PlayerYears(item);
            }
        };
    }

    @Bean
    @StepScope
    public FlatFileItemReader<Player> playerFlatFileItemReader(){
        return new FlatFileItemReaderBuilder<Player>()
                .name("playerFlatFileItemReader")
                .resource(new FileSystemResource("Players.csv")) //player.csv 파일을
                .lineTokenizer(new DelimitedLineTokenizer())  //   " , " 단위로 나눌것이다.
                .fieldSetMapper(new PlayerFieldsSetMapper())  //  PlayerFieldsSetMapper에 있는 메서드 확인
                .linesToSkip(1)
                .build();
    }

}
