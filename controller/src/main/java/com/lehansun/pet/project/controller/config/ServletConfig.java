package com.lehansun.pet.project.controller.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@ComponentScan({"com.lehansun.pet.project.controller"})
@EnableWebMvc
@EnableTransactionManagement
@RequiredArgsConstructor
public class ServletConfig implements WebMvcConfigurer {

    private final ApplicationContext applicationContext;

}