package batch.persistence;

import batch.domain.Dumpling;
import batch.domain.DumplingType;
import batch.wiring.BatchConfig;
import com.google.common.collect.Lists;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = BatchConfig.class)
public class MongoIntegrationTest {
    private Logger log = LoggerFactory.getLogger(getClass());

    private ApplicationContext context;

    private MongoTemplate mongoTemplate;

    @Before
    public void setUp() {
        mongoTemplate.dropCollection(Dumpling.class);
    }

    @After
    public void cleanup() {
        mongoTemplate.dropCollection(Dumpling.class);
    }

    @Test
    public void shouldStoreDumplingsInMongo() {
        List<String> beans = Lists.newArrayList(context.getBeanDefinitionNames());
        beans.forEach(log::info);

        mongoTemplate.save(new Dumpling(DumplingType.Beef));
        List<Dumpling> dumplings = mongoTemplate.findAll(Dumpling.class);
        dumplings.forEach(dumpling -> log.info(dumpling.toString()));
        assertThat(dumplings.size(), is(1));
    }


    @Autowired
    public void setContext(ApplicationContext context) {
        this.context = context;
    }

    @Autowired
    public void setMongoTemplate(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }
}
