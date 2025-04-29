package com.codesnippet.SpringBatchDemo.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

import java.io.File;
import java.nio.file.Path;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

@Slf4j
public class MyBatchJobTasklet implements Tasklet {

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {

        log.info("Batch started ");


        List<String> lines = Arrays.asList("A", "B", "C", "D", "E", "F", "G", "H");
        String folder = "C:\\Users\\anon\\Desktop\\batch";
        LocalTime now = LocalTime.now();

        String fileName = folder + "\\file_" + now.getHour() + "_" + now.getMinute()
                + "_" + now.getSecond() + ".txt";

        File file = new File(fileName);
        log.info("Batch File name: {} ", fileName);

        FileUtils.writeLines(file, lines);

        log.info("Batch Ended ");
        return null;

    }
}
