package com.example.springbatchtemplate.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;

@Slf4j
public class JobLoggerListener implements JobExecutionListener {
    private static final String BEFORE_MESSAGE = "Start Job({}) instance {}";

    private static final String AFTER_MESSAGE = "End Job({}) instance {}";

    @Override
    public void beforeJob(JobExecution jobExecution) {
        log.info(BEFORE_MESSAGE, jobExecution.getJobInstance().getJobName(), jobExecution.getJobInstance().getInstanceId());
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        log.info(AFTER_MESSAGE, jobExecution.getJobInstance().getJobName(), jobExecution.getJobInstance().getInstanceId());
    }
}
