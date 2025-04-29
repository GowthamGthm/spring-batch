package com.codesnippet.SpringBatchDemo.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class BatchConfig {

    @Autowired
    JobRepository jobRepository;

    @Autowired
    PlatformTransactionManager transactionManager;


    @Bean(name = "create_file_job_task_letjob")
    public Job myBatchJobTaskletJob() {
        return new JobBuilder("create_file_job_task_letjob", jobRepository)
                .start(myBatchJobStep())
                .build();
    }

    @Bean
    public Step myBatchJobStep() {
        return new StepBuilder("myBatchJobStep", jobRepository)
                .tasklet(myBatchJobTasklet(), transactionManager)
                .build();
    }

    @Bean
    public MyBatchJobTasklet myBatchJobTasklet() {
        return new MyBatchJobTasklet();
    }


}