package batch.util;

import batch.wiring.BatchConfig;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;

import javax.sql.DataSource;
import java.sql.SQLException;

public class SpringBatchSchemaGenerator {
    public static void main(String[] args) throws SQLException {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(BatchConfig.class);
        ResourceDatabasePopulator dbPopulator = new ResourceDatabasePopulator(new ClassPathResource("springBatchSchema.sql"));
        dbPopulator.populate(context.getBean("dataSource", DataSource.class).getConnection());
    }
}
