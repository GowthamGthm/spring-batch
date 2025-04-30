package com.codesnippet.SpringBatchDemo.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.time.LocalTime;
import java.util.*;

@Slf4j
public class MyBatchJobTasklet implements Tasklet {


    @Autowired
    SftpProperties properties;

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        log.info("ALL PROPS: {}", properties.toString());

        ExecutionContext executionContext = chunkContext.getStepContext().getStepExecution().getJobExecution().getExecutionContext();
        Map<String, Object> jobParameters = chunkContext.getStepContext().getJobParameters();
        String jobName = Optional.ofNullable(jobParameters.get("argBatchJobName")).orElse("").toString();
        log.info("Batch started ");

        List<String> lines = new ArrayList<>(List.of("A", "B", "C", "D", "E", "F", "G", "H"));
        String folder = "C:\\Users\\anon\\Desktop\\batch";
        LocalTime now = LocalTime.now();

        String fileName = folder + "\\file_" + now.getHour() + "_" + now.getMinute() + "_" + now.getSecond() + ".txt";

        File file = new File(fileName);
        log.info("Batch File name: {} ", fileName);

        String lowerJobNAme = jobName.toLowerCase();

        StringBuilder builder = new StringBuilder();
        builder.append(System.lineSeparator()).append(properties.getProperty(lowerJobNAme + "." + "props1"));
        builder.append(System.lineSeparator()).append(properties.getProperty(lowerJobNAme + "." + "props2"));
        builder.append(System.lineSeparator()).append(properties.getProperty(lowerJobNAme + "." + "props3"));
        builder.append(System.lineSeparator()).append(properties.getProperty(lowerJobNAme + "." + "props4"));
        builder.append(System.lineSeparator()).append(properties.getProperty(lowerJobNAme + "." + "props5"));

        lines.add(builder.toString());

        FileUtils.writeLines(file, lines);

        log.info("Batch Ended ");
        executionContext.put("status", "SUCCESS");
        return RepeatStatus.FINISHED;

    }

}