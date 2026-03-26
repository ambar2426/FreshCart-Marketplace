package com.freshcart.marketplace.infrastructure.config;

import java.util.Properties;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
public class DatabaseConfig {

    @Value("${db.driver:org.h2.Driver}")
    private String driverClassName;

    @Value("${db.url:jdbc:h2:file:/app/data/freshcart;DB_CLOSE_DELAY=-1;AUTO_SERVER=TRUE;MODE=MySQL}")
    private String jdbcUrl;

    @Value("${db.username:sa}")
    private String dbUsername;

    @Value("${db.password:}")
    private String dbPassword;

    @Value("${hibernate.dialect:org.hibernate.dialect.H2Dialect}")
    private String hibernateDialect;

    @Value("${hibernate.show_sql:true}")
    private String showSql;

    @Value("${hibernate.hbm2ddl.auto:update}")
    private String ddlStrategy;

    @Value("${entitymanager.packagesToScan:com.freshcart.marketplace}")
    private String scanPackages;

    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource ds = new DriverManagerDataSource();
        ds.setDriverClassName(this.driverClassName);
        ds.setUrl(this.jdbcUrl);
        ds.setUsername(this.dbUsername);
        ds.setPassword(this.dbPassword);
        return ds;
    }

    @Bean(name = "sessionFactory")
    public LocalSessionFactoryBean sessionFactory() {
        LocalSessionFactoryBean factory = new LocalSessionFactoryBean();
        factory.setDataSource(dataSource());
        factory.setPackagesToScan(this.scanPackages);

        Properties ormSettings = new Properties();
        if (this.hibernateDialect != null) ormSettings.put("hibernate.dialect", this.hibernateDialect);
        if (this.showSql != null) ormSettings.put("hibernate.show_sql", this.showSql);
        if (this.ddlStrategy != null) ormSettings.put("hibernate.hbm2ddl.auto", this.ddlStrategy);
        factory.setHibernateProperties(ormSettings);

        return factory;
    }

    @Bean
    public HibernateTransactionManager transactionManager(org.hibernate.SessionFactory sessionFactory) {
        HibernateTransactionManager txManager = new HibernateTransactionManager();
        txManager.setSessionFactory(sessionFactory);
        return txManager;
    }
}
