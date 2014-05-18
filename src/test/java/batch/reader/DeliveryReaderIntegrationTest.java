package batch.reader;

import batch.domain.BoxOfDumplings;
import batch.wiring.BatchConfig;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = BatchConfig.class)
public class DeliveryReaderIntegrationTest {
    private Logger log = LoggerFactory.getLogger(getClass());

    private FlatFileItemReader<BoxOfDumplings> reader;

    @Before
    public void setUp() {
        reader.open(new ExecutionContext());
    }

    @After
    public void close() {
        reader.close();
    }

    @Test
    public void shouldReadDeliveryDataCorrectly() {

        BoxOfDumplings box = readDumpling();
        while(box != null) {
            log.info(box.toString());
            box = readDumpling();
        }
    }

    private BoxOfDumplings readDumpling() {
        try {
            return reader.read();
        } catch (Exception e) {
            log.error("found a dodgy box of dumplinglings");
            return readDumpling();
        }
    }

    @Autowired
    public void setReader(FlatFileItemReader<BoxOfDumplings> reader) {
        this.reader = reader;
    }
}
