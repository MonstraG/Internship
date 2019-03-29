package com.spring.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

@Configuration
@EnableTransactionManagement
public class DataSourceConfig {
    
    @Bean
    public DataSource getDataSource() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:postgresql://localhost:5432/postgres");
        config.setDriverClassName("org.postgresql.Driver");
        config.setUsername("postgres");
        config.setPassword("pass");
        config.addDataSourceProperty("serverName", "localhost");
        config.addDataSourceProperty("port","5432");
        config.addDataSourceProperty("databaseName", "postgres");
        config.setPoolName("springHikariCP");

        return new HikariDataSource(config);
    }

    @Bean
    public JdbcTemplate LocationDAOImpl(DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    /**
     * Allows autowiring DataSourceTransactionManager, which allows use of @Transactional
     * @return DataSourceTransactionManager to be picked up by Spring's autowire.
     */
    @Bean
    public DataSourceTransactionManager getDataSourceTransactionManager() {
        return new DataSourceTransactionManager(getDataSource());
    }

}