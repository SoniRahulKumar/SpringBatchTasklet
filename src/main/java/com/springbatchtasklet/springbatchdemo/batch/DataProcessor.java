package com.springbatchtasklet.springbatchdemo.batch;

import com.springbatchtasklet.springbatchdemo.config.Employee;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.step.tasklet.Tasklet;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.repeat.RepeatStatus;

public class DataProcessor implements Tasklet , StepExecutionListener {
    private final Logger logger = LoggerFactory.getLogger(DataProcessor.class);

    private List<Employee> employees;

    @Override
    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {
        for (Employee emp : employees) {
            long age = ChronoUnit.YEARS.between(emp.getDob(), LocalDate.now());
            logger.info("Calculating age {} for emp {}", age, emp);
            emp.setAge(age);
        }

        return RepeatStatus.FINISHED;
    }

    @Override
    public void beforeStep(StepExecution stepExecution) {
        ExecutionContext executionContext = stepExecution.getJobExecution().getExecutionContext();

        employees = (List<Employee>) executionContext.get("employees");
        logger.info("Employee Data Processor initialized.");
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        logger.info("Employee Data Processor ended.");
        return ExitStatus.COMPLETED;
    }
}
