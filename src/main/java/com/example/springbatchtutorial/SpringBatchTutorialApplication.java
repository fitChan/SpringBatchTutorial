package com.example.springbatchtutorial;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableBatchProcessing // 이렇게 해야 batch 프로그램이 실행이 된다.
@SpringBootApplication
public class SpringBatchTutorialApplication{

    public static void main(String[] args) {
        SpringApplication.run(SpringBatchTutorialApplication.class, args);
    }


}
