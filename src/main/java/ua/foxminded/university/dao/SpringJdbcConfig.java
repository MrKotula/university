package ua.foxminded.university.dao;

import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

@Configuration
public class SpringJdbcConfig {
    
    @Value("${spring.datasource.url}")
    private String url;
    
    @Value("${spring.datasource.username}")
    private String userName;
    
    @Value("${spring.datasource.password}")
    private String password;
    
    @Value("${spring.datasource.driverClassName}")
    private String driver;

    @Bean
    public DataSource postgresDataSource() {
	DriverManagerDataSource dataSource = new DriverManagerDataSource();
	dataSource.setUrl(url);
	dataSource.setDriverClassName(driver);
	dataSource.setUsername(userName);
	dataSource.setPassword(password);

	return dataSource;
    }
  
    @Bean
    public JdbcTemplate jdbcTemplate() {
	return new JdbcTemplate(postgresDataSource());
    }
}
