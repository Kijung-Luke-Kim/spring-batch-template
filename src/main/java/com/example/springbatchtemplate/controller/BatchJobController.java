package com.example.springbatchtemplate.controller;

import com.example.springbatchtemplate.parameter.TestTaskletJobParameter;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class BatchJobController {
    private final JobLauncher jobLauncher;
    private final Job testTaskletJob;

    private final ObjectMapper objectMapper;

    @PostMapping("/test-tasklet-job")
    public ResponseEntity<Boolean> postTestJob(
            @RequestBody TestTaskletJobParameter parameter
    ) throws JobInstanceAlreadyCompleteException, JobExecutionAlreadyRunningException, JobParametersInvalidException, JobRestartException {
        Map<String, JobParameter> jobParameters = objectMapper.convertValue(parameter, new TypeReference<Map<String, JobParameter>>() {
        });
        jobLauncher.run(testTaskletJob, new JobParameters(jobParameters));
        return new ResponseEntity<>(true, HttpStatus.OK);
    }
}
