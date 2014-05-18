package batch.processor;

import batch.domain.BoxOfDumplings;
import batch.domain.Dumpling;
import batch.domain.DumplingType;
import org.junit.Before;
import org.junit.Test;

import java.util.Collection;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

public class DeliveryProcessorTest {

    private DeliveryProcessor processor;

    @Before
    public void setup() {
        processor = new DeliveryProcessor();
    }

    @Test
    public void shouldCreateAsManyDumplingsAsInTheBox() throws Exception {
        BoxOfDumplings box = new BoxOfDumplings(DumplingType.Beef, 50);

        Collection<Dumpling> dumplings = processor.process(box);

        assertThat(dumplings.size(), is(50));
    }



}