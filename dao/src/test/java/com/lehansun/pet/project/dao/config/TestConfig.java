package com.lehansun.pet.project.dao.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@ComponentScan("com.lehansun.pet.project.dao")
@EnableTransactionManagement
public class TestConfig {
}
