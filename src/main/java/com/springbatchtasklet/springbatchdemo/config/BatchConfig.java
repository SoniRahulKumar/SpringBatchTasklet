package com.springbatchtasklet.springbatchdemo.config;

import com.springbatchtasklet.springbatchdemo.batch.*;
import org.springframework.context.annotation.Configuration;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class BatchConfig {
    @Bean
    Step readData(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("readData", jobRepository)
                .allowStartIfComplete(true)
                .tasklet(new DataReader(), transactionManager)
                .build();
    }

    @Bean
    Step processData(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("processData", jobRepository)
                .allowStartIfComplete(true)
                .tasklet(new DataProcessor(), transactionManager)
                .build();
    }

    @Bean
    Step writeData(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("writeData", jobRepository)
                .allowStartIfComplete(true)
                .tasklet(new DataWriter(), transactionManager)
                .build();
    }

//    @Bean
//    Step csvToDbStore(JobRepository jobRepository, PlatformTransactionManager transactionManager,
//                      JdbcTemplate jdbcTemplate) {
//        return new StepBuilder("csvToDbStore", jobRepository)
//                .allowStartIfComplete(true)
//                .tasklet(new CsvToDbTasklet(), transactionManager)
//                .build();
//    }

    @Bean
    public CsvToDbTasklet csvToDbTasklet() {
        return new CsvToDbTasklet();
    }

    @Bean
    Step csvToDbStore(JobRepository jobRepository, PlatformTransactionManager transactionManager,
                      JdbcTemplate jdbcTemplate) {
        return new StepBuilder("csvToDbStore", jobRepository)
                .allowStartIfComplete(true)
                .tasklet(csvToDbTasklet()
                        , transactionManager)
                .build();
    }

    @Bean
    Job job(JobRepository jobRepository, PlatformTransactionManager transactionManager, JdbcTemplate jdbcTemplate) {
        return new JobBuilder("job", jobRepository)
                .start(readData(jobRepository, transactionManager))
                .next(processData(jobRepository, transactionManager))
                .next(writeData(jobRepository, transactionManager))
                .next(csvToDbStore(jobRepository, transactionManager, jdbcTemplate))
                //.next(fileZipAndDeleting(jobRepository, transactionManager))
                .build();
    }

    @Bean
    Step fileZipAndDeleting(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("fileZipAndDeleting", jobRepository)
                .allowStartIfComplete(true)
                .tasklet(new FileZipandDeletingTasklet(), transactionManager)
                .build();
    }


}
