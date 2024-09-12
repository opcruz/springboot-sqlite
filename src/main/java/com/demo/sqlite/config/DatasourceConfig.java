package com.demo.sqlite.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

@Configuration
public class DatasourceConfig {
   @Value("${spring.datasource.driver-class-name}")
   private String driverClassName;
   @Value("${spring.datasource.url}")
   private String dataSourceUrl;

   @Bean
   public DataSource dataSource() {
      DriverManagerDataSource dataSource = new DriverManagerDataSource();
      dataSource.setDriverClassName(driverClassName);
      dataSource.setUrl(dataSourceUrl);
      return dataSource;
   }

}
