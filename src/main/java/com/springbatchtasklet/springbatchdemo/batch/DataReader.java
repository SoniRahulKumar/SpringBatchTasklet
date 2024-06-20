package com.springbatchtasklet.springbatchdemo.batch;

import java.util.ArrayList;
import java.util.List;

import com.springbatchtasklet.springbatchdemo.config.Employee;
import com.springbatchtasklet.springbatchdemo.util.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

public class DataReader implements Tasklet, StepExecutionListener{

    private final Logger logger = LoggerFactory.getLogger(DataReader.class);

    private List<Employee> employees;
    private FileUtils fileUtil;

    @Override
    public void beforeStep(StepExecution stepExecution) {
        employees = new ArrayList<>();
        //fileUtil = new FileUtils("D:/IntelliJWS/POC/spring-batch-tasklet/src/main/resources/static/employee.csv");
        fileUtil = new FileUtils("employee.csv");
        logger.info("Data Reader initialized.");
    }

    @Override
    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {
        Employee data = fileUtil.readLine();

        while (data != null) {
            employees.add(data);
            logger.info("Read data: " + data);
            data = fileUtil.readLine();
        }

        return RepeatStatus.FINISHED;
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        fileUtil.closeReader();

        stepExecution
                .getJobExecution()
                .getExecutionContext()
                .put("employees", employees);

        logger.info("Data Reader ended.");

        return ExitStatus.COMPLETED;
    }
}
