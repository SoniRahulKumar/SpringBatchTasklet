package com.springbatchtasklet.springbatchdemo.batch;

import com.springbatchtasklet.springbatchdemo.entity.EmployeeEntity;
import com.springbatchtasklet.springbatchdemo.repository.EmployeeRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

@Component
public class CsvToDbTasklet  implements Tasklet {
    private final Logger logger = LoggerFactory.getLogger(CsvToDbTasklet.class);
    //@Autowired
//    private final JdbcTemplate jdbcTemplate;// = new JdbcTemplate();

    private final String INSERT_SQL = "INSERT INTO employee (name, age) VALUES (?, ?)";

    @Autowired
    private EmployeeRepo employeeRepo;
//    @Autowired
//    public void setEmployeeRepo(EmployeeRepo employeeRepo) {
//        this.employeeRepo = employeeRepo;
//    }


//    @Autowired
//    public CsvToDbTasklet(JdbcTemplate jdbcTemplate) {
//        this.jdbcTemplate = jdbcTemplate;
//    }

    @Override
    public RepeatStatus execute(StepContribution context, ChunkContext chunkContext) throws Exception {
        EmployeeEntity entity = new EmployeeEntity();
        // Replace "path/to/your/file.csv" with your actual file path
        File csvFile = new File("D:\\IntelliJWS\\POC\\spring-batch-tasklet\\src\\main\\resources\\output.csv");
        try (BufferedReader reader = new BufferedReader(new FileReader(csvFile))) {
            String line;
            //reader.readLine(); // Skip header row (assuming first row has column names)
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                // Assuming name and email are in the first two columns (adjust as needed)
                String name = data[0];
                String age = data[1];
                logger.info("Employee1 "+ name+" "+age );
                entity.setName(name);
                entity.setAge(age);
                employeeRepo.save(entity);
                //jdbcTemplate.update(INSERT_SQL, name, age);
                logger.info("Employee "+ name+" "+age );
            }
        }
        return RepeatStatus.FINISHED;
    }
}
