package com.lehansun.pet.project.service.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
@ComponentScan(basePackages = "com.lehansun.pet.project.service")
public class ServiceConfig {

    @Bean
    public ModelMapper getModelMapper(){
        return new ModelMapper();
    }
}
