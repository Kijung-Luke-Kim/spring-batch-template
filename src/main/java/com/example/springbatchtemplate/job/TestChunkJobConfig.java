package com.example.springbatchtemplate.job;

import com.example.springbatchtemplate.entity.HttpJobExecution;
import com.example.springbatchtemplate.listener.JobLoggerListener;
import com.example.springbatchtemplate.parameter.TestChunkJobParameter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.batch.item.database.PagingQueryProvider;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.database.builder.JdbcPagingItemReaderBuilder;
import org.springframework.batch.item.database.support.SqlPagingQueryProviderFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.BeanPropertyRowMapper;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class TestChunkJobConfig {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final TestChunkJobParameter testChunkJobParameter;
    private final DataSource quartzDataSource;

    @Bean
    @JobScope
    public TestChunkJobParameter testChunkJobParameter() {
        return new TestChunkJobParameter();
    }

    @Bean
    public Job testChunkJob() throws Exception {
        return jobBuilderFactory.get("testChunkJob")
                .incrementer(new RunIdIncrementer())
                .listener(new JobLoggerListener())
                .start(testChunkStep(null, null, null))
                .build();
    }

    @JobScope
    @Bean
    public Step testChunkStep(
            ItemReader testReader,
            ItemProcessor testProcessor,
            ItemWriter testWriter
    ) throws Exception {
        return stepBuilderFactory.get("testChunkStep")
                .chunk(testChunkJobParameter.getChunkSize())
                .reader(testReader)
                .processor(testProcessor)
                .writer(testWriter)
                .build();
    }

    @StepScope
    @Bean
    public JdbcPagingItemReader<HttpJobExecution> testReader() throws Exception {
        Map<String, Object> params = new HashMap<>();
        params.put("jobName", testChunkJobParameter.getJobName());

        return new JdbcPagingItemReaderBuilder<HttpJobExecution>()
                .pageSize(testChunkJobParameter().getChunkSize())
                .dataSource(quartzDataSource)
                .rowMapper(new BeanPropertyRowMapper<>(HttpJobExecution.class))
                .queryProvider(testQueryProvider())
                .parameterValues(params)
                .name("testReader")
                .build();
    }

    @StepScope
    @Bean
    public ItemProcessor<HttpJobExecution, HttpJobExecution> testProcessor() {
        return item -> {
            log.info("HttpJobExecution {}", item.getInstanceId());
            return item;
        };
    }

    @StepScope
    @Bean
    public JdbcBatchItemWriter<HttpJobExecution> testWriter() {
        return new JdbcBatchItemWriterBuilder<HttpJobExecution>()
                .dataSource(quartzDataSource)
                .sql("update quartz.http_job_execution set status_code = :statusCode where instance_id = :instanceId")
                .beanMapped()
                .build();
    }

    private PagingQueryProvider testQueryProvider() throws Exception {
        SqlPagingQueryProviderFactoryBean queryProvider = new SqlPagingQueryProviderFactoryBean();
        queryProvider.setDataSource(quartzDataSource);
        queryProvider.setSelectClause("SELECT *");
        queryProvider.setFromClause("FROM quartz.HTTP_JOB_EXECUTION");
        queryProvider.setWhereClause("WHERE JOB_NAME = :jobName");
        queryProvider.setSortKey("INSTANCE_ID");
        return queryProvider.getObject();
    }
}
