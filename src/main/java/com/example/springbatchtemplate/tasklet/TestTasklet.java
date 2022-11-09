package com.example.springbatchtemplate.tasklet;

import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

@Slf4j
@Builder
public class TestTasklet implements Tasklet {
    private String name;

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        log.info("Executing test tasklet");
        log.info("Tasklet name: {}", name);
        return RepeatStatus.FINISHED;
    }
}
