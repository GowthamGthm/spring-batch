package com.codesnippet.SpringBatchDemo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import java.util.Date;

@SpringBootApplication
@Slf4j
public class SpringBatchDemoApplication {

    public static void main(String[] args) {

        SpringApplication app = new SpringApplication(SpringBatchDemoApplication.class);
        ApplicationContext context = app.run(args);

        JobLauncher jobLauncher = context.getBean(JobLauncher.class);
        JobParameters jobParameters = new JobParametersBuilder()
                .addDate("date", new Date())
                .addString("argBatchJobName", args[2])
                .toJobParameters();

        try {
            Job job = context.getBean(args[2], Job.class);
            JobExecution jobExecution = jobLauncher.run(job, jobParameters);
        } catch (Exception e) {
            log.error("error in batch " , e);
        } finally {
            System.exit(SpringApplication.exit(context));
        }


    }

}