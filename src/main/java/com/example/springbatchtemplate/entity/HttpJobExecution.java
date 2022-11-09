package com.example.springbatchtemplate.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Slf4j
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class HttpJobExecution {
    @JsonProperty("START_TIME")
    private LocalDateTime startTime;
    @JsonProperty("END_TIME")
    private LocalDateTime endTime;
    @JsonProperty("STATUS_CODE")
    private HttpStatus statusCode;
    @JsonProperty("RESPONSE")
    private String response;
    @JsonProperty("JOB_NAME")
    private String jobName;
    @JsonProperty("JOB_GROUP")
    private String jobGroup;
    @JsonProperty("INSTANCE_ID")
    private Long instanceId;

    public String process() {
        return instanceId != null ? instanceId.toString() : "null";
    }
}
