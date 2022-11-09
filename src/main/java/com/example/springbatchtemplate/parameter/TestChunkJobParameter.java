package com.example.springbatchtemplate.parameter;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;

@Getter
@NoArgsConstructor
public class TestChunkJobParameter {
    @Value("#{jobParameters[chunkSize]}")
    private Integer chunkSize;
    @Value("#{jobParameters[jobName]}")
    private String jobName;
}
