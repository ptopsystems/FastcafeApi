package com.rest.api.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.HashMap;

@Configuration
@RequiredArgsConstructor
@PropertySource(value = "classpath:application.yml")
@EnableJpaRepositories(
        entityManagerFactoryRef = "fastCafeLogEntityManager",
        transactionManagerRef = "fastCafeLogTransactionManager",
        basePackages = {"com.rest.api.repository.fastcafe_log"}
)

public class FastCafeLogDbConfig {

    private final Environment env;

    @Bean(name = "fastCafeLogDataSource")
    @ConfigurationProperties(prefix = "datasource.fastcafe-log")
    public DataSource fastcafeLogDataSource(){
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setUrl(env.getProperty("datasource.fastcafe-log.url"));
        dataSource.setUsername(env.getProperty("datasource.fastcafe-log.username"));
        dataSource.setPassword(env.getProperty("datasource.fastcafe-log.password"));
        dataSource.setDriverClassName(env.getProperty("datasource.fastcafe-log.driver-class-name"));

        return dataSource;
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean fastCafeLogEntityManager(){
        LocalContainerEntityManagerFactoryBean entityManager = new LocalContainerEntityManagerFactoryBean();
        entityManager.setDataSource(fastcafeLogDataSource());
        entityManager.setPackagesToScan("com.rest.api.entity.fastcafe_log");
        entityManager.setPersistenceUnitName("fastcafelog");

        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        entityManager.setJpaVendorAdapter(vendorAdapter);

        HashMap<String, Object> properties = new HashMap<>();
        properties.put("hibernate.show_sql", env.getProperty("spring.jpa.show-sql"));
        properties.put("hibernate.dialect", env.getProperty("spring.jpa.properties.hibernate.dialect"));

        entityManager.setJpaPropertyMap(properties);
        entityManager.afterPropertiesSet();

        return entityManager;
    }

    @Bean
    public PlatformTransactionManager fastCafeLogTransactionManager(){

        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(fastCafeLogEntityManager().getObject());
        return transactionManager;
    }
}
