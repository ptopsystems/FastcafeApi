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
        entityManagerFactoryRef = "fastcafeAdminEntityManager",
        transactionManagerRef = "fastcafeAdminTransactionManager",
        basePackages = {"com.rest.api.repository.fastcafe_admin"}
)

public class FastcafeAdminDbConfig {

    private final Environment env;

    @Primary
    @Bean(name = "fastcafeAdminDataSource")
    @ConfigurationProperties(prefix = "datasource.fastcafe-admin")
    public DataSource fastcafeAdminDataSource(){
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setUrl(env.getProperty("datasource.fastcafe-admin.url"));
        dataSource.setUsername(env.getProperty("datasource.fastcafe-admin.username"));
        dataSource.setPassword(env.getProperty("datasource.fastcafe-admin.password"));
        dataSource.setDriverClassName(env.getProperty("datasource.fastcafe-admin.driver-class-name"));

        return dataSource;
    }

    @Primary
    @Bean
    public LocalContainerEntityManagerFactoryBean fastcafeAdminEntityManager(){
        LocalContainerEntityManagerFactoryBean entityManager = new LocalContainerEntityManagerFactoryBean();
        entityManager.setDataSource(fastcafeAdminDataSource());
        entityManager.setPackagesToScan("com.rest.api.entity.fastcafe_admin");
        entityManager.setPersistenceUnitName("fastcafeadmin");

        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        entityManager.setJpaVendorAdapter(vendorAdapter);

        HashMap<String, Object> properties = new HashMap<>();
        properties.put("hibernate.show_sql", env.getProperty("spring.jpa.show-sql"));
        properties.put("hibernate.dialect", env.getProperty("spring.jpa.properties.hibernate.dialect"));

        entityManager.setJpaPropertyMap(properties);
        entityManager.afterPropertiesSet();

        return entityManager;
    }

    @Primary
    @Bean
    public PlatformTransactionManager fastcafeAdminTransactionManager(){

        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(fastcafeAdminEntityManager().getObject());
        return transactionManager;
    }
}
