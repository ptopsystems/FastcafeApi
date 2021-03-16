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
        entityManagerFactoryRef = "fastCafeStatEntityManager",
        transactionManagerRef = "fastCafeStatTransactionManager",
        basePackages = {"com.rest.api.repository.fastcafe_stat"}
)

public class FastCafeStatDbConfig {

    private final Environment env;

    @Bean(name = "fastCafeStatDataSource")
    @ConfigurationProperties(prefix = "datasource.fastcafe-stat")
    public DataSource fastCafeStatDataSource(){
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setUrl(env.getProperty("datasource.fastcafe-stat.url"));
        dataSource.setUsername(env.getProperty("datasource.fastcafe-stat.username"));
        dataSource.setPassword(env.getProperty("datasource.fastcafe-stat.password"));
        dataSource.setDriverClassName(env.getProperty("datasource.fastcafe-stat.driver-class-name"));

        return dataSource;
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean fastCafeStatEntityManager(){
        LocalContainerEntityManagerFactoryBean entityManager = new LocalContainerEntityManagerFactoryBean();
        entityManager.setDataSource(fastCafeStatDataSource());
        entityManager.setPackagesToScan("com.rest.api.entity.fastcafe_stat");
        entityManager.setPersistenceUnitName("fastcafestat");

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
    public PlatformTransactionManager fastCafeStatTransactionManager(){

        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(fastCafeStatEntityManager().getObject());
        return transactionManager;
    }
}
