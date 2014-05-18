package batch.wiring;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

@Configuration
public class JdbcConfig {

    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        //dataSource.setDriverClassName();
        dataSource.setUrl("jdbc:postgresql://localhost:5432/springbatch");
        dataSource.setUsername("springbatch");
        dataSource.setPassword("springbatch");

        return dataSource;
    }
}
