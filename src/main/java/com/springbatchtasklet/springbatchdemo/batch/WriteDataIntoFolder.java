package com.springbatchtasklet.springbatchdemo.batch;

import com.springbatchtasklet.springbatchdemo.config.Employee;
import com.springbatchtasklet.springbatchdemo.entity.EmployeeEntity;
import com.springbatchtasklet.springbatchdemo.util.FileUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class WriteDataIntoFolder implements Tasklet {
    private List<Employee> employees;
    private FileUtils fileUtil;
    @Autowired
    StepExecution stepExecution;

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        Employee data = fileUtil.readLine();

        readerTask();
        dataProcessorTask();
        writerTask();
        return RepeatStatus.FINISHED;
    }

    private void writerTask() {
        ExecutionContext executionContext = stepExecution
                .getJobExecution()
                .getExecutionContext();

        employees = (List<Employee>) executionContext.get("employees");
        fileUtil = new FileUtils("D:\\IntelliJWS\\POC\\spring-batch-tasklet\\src\\main\\resources\\output.csv");
        log.info("Data Writer initialized.");

        EmployeeEntity employee = new EmployeeEntity();
        for (Employee data : employees) {
            fileUtil.writeLine(data);
            log.info("Write data {} ", data);
        }
        fileUtil.closeWriter();
        log.info("Data Writer ended.");
    }

    private void dataProcessorTask() {
        ExecutionContext executionContext = stepExecution.getJobExecution().getExecutionContext();

        employees = (List<Employee>) executionContext.get("employees");
        log.info("Employee Data Processor initialized.");

        for (Employee emp : employees) {
            long age = ChronoUnit.YEARS.between(emp.getDob(), LocalDate.now());
            log.info("Calculating age {} for emp {}", age, emp);
            emp.setAge(age);
        }

        log.info("Employee Data Processor ended.");
    }

    private void readerTask() {
        employees = new ArrayList<>();
        //fileUtil = new FileUtils("D:/IntelliJWS/POC/spring-batch-tasklet/src/main/resources/static/employee.csv");
        fileUtil = new FileUtils("employee.csv");
        log.info("Data Reader initialized.");

        Employee data = fileUtil.readLine();
        while (data != null) {
            employees.add(data);
            log.info("Read data: " + data);
            data = fileUtil.readLine();
        }

        fileUtil.closeReader();

        stepExecution
                .getJobExecution()
                .getExecutionContext()
                .put("employees", employees);

        log.info("Data Reader ended.");
    }
}
