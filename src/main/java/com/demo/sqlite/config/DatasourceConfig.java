package com.demo.sqlite.config;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class DatasourceConfig {
   @Value("${spring.datasource.driver-class-name}")
   private String driverClassName;
   @Value("${spring.datasource.url}")
   private String dataSourceUrl;

   @Bean
   public DataSource dataSource() {
      HikariDataSource dataSource = new HikariDataSource();
      dataSource.setDriverClassName(driverClassName);
      dataSource.setJdbcUrl(dataSourceUrl);
      dataSource.setMaximumPoolSize(10);
      dataSource.setConnectionTimeout(60000);
      dataSource.setConnectionTestQuery("SELECT name FROM sqlite_master limit 0;");
      return dataSource;
   }

}
