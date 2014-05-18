package batch.wiring;

import batch.SpringBatchDumplingApp;
import batch.domain.BoxOfDumplings;
import batch.domain.Dumpling;
import batch.processor.DeliveryProcessor;
import batch.writer.DumplingWriter;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.explore.support.JobExplorerFactoryBean;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.FlatFileParseException;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.mongodb.core.MongoTemplate;

import javax.sql.DataSource;
import java.util.Collection;

@Configuration
@EnableBatchProcessing
@Import({JdbcConfig.class, MongoConfig.class})
public class BatchConfig {

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    private DataSource dataSource;

    public JobExplorer jobExplorer() throws Exception {
        JobExplorerFactoryBean factory = new JobExplorerFactoryBean();
        factory.setDataSource(dataSource);
        factory.afterPropertiesSet();
        return factory.getObject();
    }

    @Autowired
    private MongoTemplate mongoTemplate;

    @Bean
    public SpringBatchDumplingApp app() throws Exception {
        return new SpringBatchDumplingApp(jobLauncher, jobExplorer());
    }

    //readers
    @Bean
    public FlatFileItemReader<BoxOfDumplings> deliveryReader() {
        FlatFileItemReader<BoxOfDumplings> reader = new FlatFileItemReader<BoxOfDumplings>();
        reader.setResource(new ClassPathResource("Delivery.csv"));
        reader.setLineMapper(new DefaultLineMapper<BoxOfDumplings>() {{
            setLineTokenizer(new DelimitedLineTokenizer() {{
                setNames(new String[] { "type", "dumplings" });
            }});
            setFieldSetMapper(new BeanWrapperFieldSetMapper<BoxOfDumplings>() {{
                setTargetType(BoxOfDumplings.class);
            }});
        }});
        return reader;
    }

    //processors
    @Bean
    public DeliveryProcessor deliveryProcessor() {
        return new DeliveryProcessor();
    }

    //writer
    @Bean
    public DumplingWriter dumplingWriter() {
        return new DumplingWriter(mongoTemplate);
    }


    //steps
    @Bean
    public Step deliveryStep() {
        return stepBuilderFactory.get("deliveryStep")
                .<BoxOfDumplings, Collection<Dumpling>> chunk(10)
                .reader(deliveryReader())
                .processor(deliveryProcessor())
                .writer(dumplingWriter())
                .faultTolerant()
                .skipLimit(10)
                .skip(FlatFileParseException.class)
                .build();
    }

    //jobs
    @Bean
    public Job dumplingDeliveryJob() {
        return jobBuilderFactory.get("dumplingDeliveryJob")
                .incrementer(new RunIdIncrementer())
                .flow(deliveryStep())
                .end()
                .build();
    }
}
