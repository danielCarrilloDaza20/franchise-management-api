package com.company.franchise.franchisemanagementapi.infrastructure.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;

@Configuration
@EnableR2dbcRepositories(basePackages = "com.company.franchise.franchisemanagementapi.infrastructure.out.persistence.repository")
public class R2dbcConfig {
}
