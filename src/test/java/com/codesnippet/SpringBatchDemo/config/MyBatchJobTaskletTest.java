package com.codesnippet.SpringBatchDemo.config;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.scope.context.StepContext;
import org.springframework.batch.item.ExecutionContext;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MyBatchJobTaskletTest {

    @InjectMocks
    private MyBatchJobTasklet myBatchJobTasklet;

    @Mock
    private SftpProperties sftpProperties;

    @Mock
    StepContribution stepContribution;

    @Mock
    ChunkContext chunkContext;

    @Mock
    StepExecution stepExecution;

    private ExecutionContext executionContext;


    @BeforeEach
    void setUp() {
        System.setProperty("CONFIG_ENV", "test");

        executionContext = new ExecutionContext();
        JobExecution jobExecution = mock(JobExecution.class);
        StepContext stepContext = mock(StepContext.class);

        lenient().when(chunkContext.getStepContext()).thenReturn(stepContext);
        lenient().when(stepContext.getStepExecution()).thenReturn(stepExecution);
        lenient().when(stepExecution.getJobExecution()).thenReturn(jobExecution);
        lenient().when(jobExecution.getExecutionContext()).thenReturn(executionContext);
        lenient().when(stepExecution.getExecutionContext()).thenReturn(executionContext);

    }

    @Test
    void testExecute() throws Exception {
        // Mocking job parameters
        Map<String, Object> jobParameters = new HashMap<>();
        jobParameters.put("argBatchJobName", "testJob");

        // Mocking properties
        when(chunkContext.getStepContext().getJobParameters()).thenReturn(jobParameters);

        when(sftpProperties.getProperty("testjob.props1")).thenReturn("value1");
        when(sftpProperties.getProperty("testjob.props2")).thenReturn("value2");
        when(sftpProperties.getProperty("testjob.props3")).thenReturn("value3");
        when(sftpProperties.getProperty("testjob.props4")).thenReturn("value4");
        when(sftpProperties.getProperty("testjob.props5")).thenReturn("value5");

        // Mocking file writing
        File mockFile = mock(File.class);

        try(var mockedFileUtils = mockStatic(FileUtils.class)) {
            mockedFileUtils.when(() -> FileUtils.writeLines(eq(mockFile), anyList()))
                    .thenAnswer(invocation -> null);
        }

        // Execute the tasklet
        myBatchJobTasklet.execute(stepContribution, chunkContext);

        // Verify interactions
        verify(sftpProperties, times(1)).getProperty("testjob.props1");
        verify(sftpProperties, times(1)).getProperty("testjob.props2");
        verify(sftpProperties, times(1)).getProperty("testjob.props3");
        verify(sftpProperties, times(1)).getProperty("testjob.props4");
        verify(sftpProperties, times(1)).getProperty("testjob.props5");
    }


    @Test
    void testExecuteOne() throws Exception {
        // Mocking job parameters
        Map<String, Object> jobParameters = new HashMap<>();
        jobParameters.put("argBatchJobName", "testJob");

        // Mocking properties
        when(chunkContext.getStepContext().getJobParameters()).thenReturn(jobParameters);

        SftpProperties properties = new SftpProperties();
        Properties props = new Properties();
        props.setProperty("testjob.props1", "value1");
        props.setProperty("testjob.props2", "value2");
        props.setProperty("testjob.props3", "value3");
        props.setProperty("testjob.props4", "value4");
        props.setProperty("testjob.props5", "value5");
        properties.properties.putAll(props);

        myBatchJobTasklet.properties = properties;

        // Mocking file writing
        File mockFile = mock(File.class);

        try(var mockedFileUtils = mockStatic(FileUtils.class)) {
            mockedFileUtils.when(() -> FileUtils.writeLines(eq(mockFile), anyList()))
                    .thenAnswer(invocation -> null);
        }

        // Execute the tasklet
        myBatchJobTasklet.execute(stepContribution, chunkContext);

        ExecutionContext executionContext = stepExecution.getJobExecution().getExecutionContext();
        assertEquals("SUCCESS", executionContext.get("status"));
        System.out.println("===============================");

    }
}