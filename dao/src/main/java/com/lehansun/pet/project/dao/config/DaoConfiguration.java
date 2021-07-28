package com.lehansun.pet.project.dao.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@ComponentScan(basePackages = "com.lehansun.pet.project.dao")
@PropertySource("classpath:/dao.properties")
public class DaoConfiguration {

    @Value("${database.url}")
    private String databaseUrl;
    @Value("${database.username}")
    private String username;
    @Value("${database.password}")
    private String password;
    @Value("${database.driverClassName}")
    private String driverClassName;

    @Value("${hibernate.show_sql}")
    private String showSql;
    @Value("${hibernate.dialect}")
    private String dialect;
    @Value("${hibernate.enable_lazy_load_no_trans}")
    private String lazyLoad;
    @Value("${hibernate.use_jdbc_metadata_defaults}")
    private String jdbcMetadataDefaults;

    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource driverManagerDataSource = new DriverManagerDataSource(databaseUrl, username, password);
        driverManagerDataSource.setDriverClassName(driverClassName);
        return driverManagerDataSource;
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        LocalContainerEntityManagerFactoryBean entityManagerFactory = new LocalContainerEntityManagerFactoryBean();
        entityManagerFactory.setDataSource(dataSource());
        entityManagerFactory.setPackagesToScan("com.lehansun.pet.project.model");
        JpaVendorAdapter adapter = new HibernateJpaVendorAdapter();
        entityManagerFactory.setJpaVendorAdapter(adapter);

        Properties properties = new Properties();
        properties.setProperty("hibernate.show_sql", showSql);
        properties.setProperty("hibernate.dialect", dialect);
        properties.setProperty("hibernate.enable_lazy_load_no_trans", lazyLoad);
        properties.setProperty("hibernate.use_jdbc_metadata_defaults", jdbcMetadataDefaults);
        entityManagerFactory.setJpaProperties(properties);

        return entityManagerFactory;
    }

    @Bean
    public JpaTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(entityManagerFactory);
        return transactionManager;
    }
}
