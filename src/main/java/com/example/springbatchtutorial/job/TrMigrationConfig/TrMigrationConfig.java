package com.example.springbatchtutorial.job.TrMigrationConfig;


import com.example.springbatchtutorial.job.TrMigrationConfig.accounts.AccountRepository;
import com.example.springbatchtutorial.job.TrMigrationConfig.accounts.Accounts;
import com.example.springbatchtutorial.job.TrMigrationConfig.orders.Orders;
import com.example.springbatchtutorial.job.TrMigrationConfig.orders.OrderRepository;
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
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Sort;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;


/*
desc : 주문테이블 -->> 정산 테이블로 데이터를 이관함
RUN : spring.batch.job.names=trMigrationJob
*/


@Configuration
@RequiredArgsConstructor
public class TrMigrationConfig {

    private final OrderRepository orderRepository;
    private final AccountRepository accountRepository;
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;


    @Bean
    public Job trMigrationJob(Step trMigrationStep) {
        return jobBuilderFactory.get("trMigrationJob")
                .incrementer(new RunIdIncrementer())
                .start(trMigrationStep)
                .build();
    }

    @Bean
    @JobScope
    public Step trMigrationStep(ItemReader trOrdersReader,
                                ItemProcessor trOrderProcessor,
                                ItemWriter trOrderWriter) {
        return stepBuilderFactory.get("trMigrationStep")
                .<Orders, Accounts>chunk(5)  //< 어떤데이터를 읽어올지, 쓸지> chunk몇 개 사이즈로 데이터를 처리할건지. >>> 5개의 데이터를 처리할 것이며, 읽어올 데이터=Order, 쓸 데이터 = Order 타입의 데이터
                .reader(trOrdersReader)
                .processor(trOrderProcessor) //witer를 할수 있도록 프로세서를 쓸것. (메서드 생성 확인)
                .writer(trOrderWriter) //가공이 된 데이터를 쓸 수 있도록 writer도 써줌. (메서드 생성 확인)
                .build();
    }

//    @StepScope
//    @Bean
//    public RepositoryItemWriter<Accounts> trOrderWriter(){
//        return new RepositoryItemWriterBuilder<Accounts>()
//                .repository(accountRepository)
//                .methodName("save")                //이거는 아무래도 이름을 짓는게 아니고 save라는 명령어를 사용하는 것같은데 . 밑에서 새로운 Method로 save를 시켜보도록 하자
//                .build();
//    }


    @StepScope
    @Bean
    public ItemWriter<Accounts> trOrderWriter() {
        return new ItemWriter<Accounts>() {
            @Override
            public void write(List<? extends Accounts> items) throws Exception {
                items.forEach(e-> accountRepository.save(e));       //repository를 사용하는 것 이외에도 다양한 method들이 있다 . 그것을 활용해 보도록 하자
            }
        };
    }


    @StepScope
    @Bean
    public ItemProcessor<Orders, Accounts> trOrderProcessor(){
        return item -> new Accounts(item);
    }

    @StepScope
    @Bean
    public RepositoryItemReader<Orders> trOrdersReader() {  //Order 타입의 데이터를 추출을 하게 됨. 이걸 step 에 등록할것.
        return new RepositoryItemReaderBuilder<Orders>()
                .name("trOrdersReader")
                .repository(orderRepository)
                .methodName("findAll")
                .pageSize(5)  //보통 chunk 사이즈와 동일하게 가져간다.
                .arguments(Arrays.asList())  //빈 배열을 준다 ?
                .sorts(Collections.singletonMap("id", Sort.Direction.ASC))

                .build();

    }
}


