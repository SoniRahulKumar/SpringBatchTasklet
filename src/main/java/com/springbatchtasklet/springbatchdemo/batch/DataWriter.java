package com.springbatchtasklet.springbatchdemo.batch;

import com.springbatchtasklet.springbatchdemo.config.Employee;
import com.springbatchtasklet.springbatchdemo.entity.EmployeeEntity;
import com.springbatchtasklet.springbatchdemo.util.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.repeat.RepeatStatus;

import java.util.List;


public class DataWriter implements Tasklet, StepExecutionListener{
    private final Logger logger = LoggerFactory.getLogger(DataWriter.class);

    private List<Employee> employees;
    private FileUtils fileUtil;

    @Override
    public void beforeStep(StepExecution stepExecution) {
        ExecutionContext executionContext = stepExecution
                .getJobExecution()
                .getExecutionContext();

        employees = (List<Employee>) executionContext.get("employees");
        fileUtil = new FileUtils("D:\\IntelliJWS\\POC\\spring-batch-tasklet\\src\\main\\resources\\output.csv");
        logger.info("Data Writer initialized.");
    }

    @Override
    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {
        EmployeeEntity employee = new EmployeeEntity();
        for (Employee data : employees) {
            fileUtil.writeLine(data);
            logger.info("Write data {} ", data);
        }

        return RepeatStatus.FINISHED;
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        fileUtil.closeWriter();
        logger.info("Data Writer ended.");
        return ExitStatus.COMPLETED;
    }
}
