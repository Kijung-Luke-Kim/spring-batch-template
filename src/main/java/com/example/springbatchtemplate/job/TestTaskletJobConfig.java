package com.example.springbatchtemplate.job;

import com.example.springbatchtemplate.listener.JobLoggerListener;
import com.example.springbatchtemplate.parameter.TestTaskletJobParameter;
import com.example.springbatchtemplate.tasklet.TestTasklet;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class TestTaskletJobConfig {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    private final TestTaskletJobParameter testTaskletJobParameter;

    @Bean
    @JobScope
    public TestTaskletJobParameter testTaskletJobParameter() {
        return new TestTaskletJobParameter();
    }

    @Bean
    public Job testTaskletJob() {
        return jobBuilderFactory.get("testChunkJob")
                .listener(new JobLoggerListener())
                .start(testTaskletStep())
                .build();
    }

    @JobScope
    @Bean
    public Step testTaskletStep() {
        return stepBuilderFactory.get("testTaskletStep")
                .tasklet(testTasklet())
                .build();
    }

    @StepScope
    @Bean
    public Tasklet testTasklet() {
        return TestTasklet.builder()
                .name(testTaskletJobParameter.getName())
                .build();
    }
}
