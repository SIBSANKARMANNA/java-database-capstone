package com.project.back_end;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.project.back_end.repo.jpa")
@EnableMongoRepositories(basePackages = "com.project.back_end.repo.mongo")
public class ClinicManagementSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(ClinicManagementSystemApplication.class, args);
    }
}
