package com.springbatchtasklet.springbatchdemo.repository;

import com.springbatchtasklet.springbatchdemo.entity.EmployeeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

//@Repository
public interface EmployeeRepo extends JpaRepository<EmployeeEntity , Long> {
}
