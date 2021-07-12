package com.lehansun.pet.project.dao.config;

import com.lehansun.pet.project.api.dao.CustomerDao;
import com.lehansun.pet.project.api.dao.RequestDao;
import com.lehansun.pet.project.dao.CustomerJpaDao;
import com.lehansun.pet.project.dao.RequestJpaDao;
import liquibase.integration.spring.SpringLiquibase;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@EnableTransactionManagement
@PropertySource("classpath:/test.properties")
public class TestConfig {

    public static final String TEST_DB_NAME = "public";
    public static final String CHANGE_LOG_PATH = "classpath:/db/changelog/db.changelog-master.xml";
    public static final String MODEL_PACKAGE = "com.lehansun.pet.project.model";

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
    @Value("${hibernate.ddl_auto}")
    private String ddl;

    @Bean
    public RequestDao getRequestDao(EntityManager entityManager) {
        RequestJpaDao requestDao = new RequestJpaDao();
        requestDao.setEntityManager(entityManager);
        return requestDao;
    }

    @Bean
    public CustomerDao getCustomerDao(EntityManager entityManager) {
        CustomerJpaDao customerJpaDao = new CustomerJpaDao();
        customerJpaDao.setEntityManager(entityManager);
        return customerJpaDao;
    }


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
        entityManagerFactory.setPackagesToScan(MODEL_PACKAGE);
        JpaVendorAdapter adapter = new HibernateJpaVendorAdapter();
        entityManagerFactory.setJpaVendorAdapter(adapter);
        entityManagerFactory.setJpaProperties(getHibernateProperties());

        return entityManagerFactory;
    }

    @Bean
    public EntityManager entityManager(EntityManagerFactory entityManagerFactory) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        return entityManager;
    }

    @Bean
    public SpringLiquibase liquibase() {
        SpringLiquibase liquibase = new SpringLiquibase();
        liquibase.setChangeLog(CHANGE_LOG_PATH);
        liquibase.setDataSource(dataSource());
        liquibase.setDefaultSchema(TEST_DB_NAME);
        liquibase.setDropFirst(false);
        liquibase.setLiquibaseSchema(TEST_DB_NAME);

        return liquibase;
    }

    @Bean
    public JpaTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(entityManagerFactory);
        return transactionManager;
    }

    private Properties getHibernateProperties() {
        Properties properties = new Properties();
        properties.setProperty("hibernate.show_sql", showSql);
        properties.setProperty("hibernate.dialect", dialect);
        properties.setProperty("hibernate.enable_lazy_load_no_trans", lazyLoad);
        properties.setProperty("hibernate.use_jdbc_metadata_defaults", jdbcMetadataDefaults);
        properties.setProperty("hibernate.ddl_auto", ddl);
        return properties;
    }

}
