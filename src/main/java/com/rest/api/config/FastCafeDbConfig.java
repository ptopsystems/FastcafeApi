package com.rest.api.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
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
        entityManagerFactoryRef = "fastCafeEntityManager",
        transactionManagerRef = "fastCafeTransactionManager",
        basePackages = {"com.rest.api.repository.fastcafe"}
)

public class FastCafeDbConfig {

    private final Environment env;

    @Primary
    @Bean(name = "fastCafeDataSource")
    @ConfigurationProperties(prefix = "datasource.fastcafe")
    public DataSource fastCafeDataSource(){
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(env.getProperty("datasource.fastcafe.driver-class-name"));
        dataSource.setUrl(env.getProperty("datasource.fastcafe.url"));
        dataSource.setUsername(env.getProperty("datasource.fastcafe.username"));
        dataSource.setPassword(env.getProperty("datasource.fastcafe.password"));

        return dataSource;
    }

    @Primary
    @Bean
    public LocalContainerEntityManagerFactoryBean fastCafeEntityManager(){
        LocalContainerEntityManagerFactoryBean entityManager = new LocalContainerEntityManagerFactoryBean();
        entityManager.setDataSource(fastCafeDataSource());
        entityManager.setPackagesToScan("com.rest.api.entity.fastcafe");
        entityManager.setPersistenceUnitName("fastcafe_datasource");

        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        entityManager.setJpaVendorAdapter(vendorAdapter);

        HashMap<String, Object> properties = new HashMap<>();
        properties.put("hibernate.dialect", env.getProperty("spring.jpa.properties.hibernate.dialect"));
        properties.put("hibernate.show_sql", env.getProperty("spring.jpa.show-sql"));

        entityManager.setJpaPropertyMap(properties);
        entityManager.afterPropertiesSet();

        return entityManager;
    }

    @Primary
    @Bean
    public PlatformTransactionManager fastCafeTransactionManager(){

        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(fastCafeEntityManager().getObject());
        return transactionManager;
    }
}
