package com.company.franchise.franchisemanagementapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;

@SpringBootApplication
public class FranchiseManagementApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(FranchiseManagementApiApplication.class, args);
    }

}
