package com.demo.sqlite.repository;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;

import javax.sql.DataSource;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.Statement;

@TestConfiguration
public class TestConfig {

    @Value("${spring.datasource.driver-class-name}")
    private String driverClassName;
    @Value("${spring.datasource.url}")
    private String dataSourceUrl;

    @Bean
    @Profile("test")
    public DataSource dataSource() {
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setDriverClassName(driverClassName);
        dataSource.setJdbcUrl(dataSourceUrl);
        dataSource.setMaximumPoolSize(10);
        dataSource.setConnectionTimeout(60000);
        dataSource.setConnectionTestQuery("SELECT name FROM sqlite_master limit 0;");
        initialize(dataSource);
        return dataSource;
    }

    public void initialize(DataSource dataSource) {
        try (Connection connection = dataSource.getConnection()) {
            Statement statement = connection.createStatement();
            InputStream inputStream = this.getClass().getResourceAsStream("/tables-sqlite.sql");
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            StringBuilder script = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                script.append(line).append("\n");
            }
            reader.close();
            statement.executeUpdate(script.toString());
            statement.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}