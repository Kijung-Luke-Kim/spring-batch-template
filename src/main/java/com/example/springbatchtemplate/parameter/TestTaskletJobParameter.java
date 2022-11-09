package com.example.springbatchtemplate.parameter;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;

@Getter
@NoArgsConstructor
public class TestTaskletJobParameter {
    @Value("#{jobParameters[name]}")
    private String name;
}
