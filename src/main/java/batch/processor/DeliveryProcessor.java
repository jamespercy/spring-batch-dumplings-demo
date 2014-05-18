package batch.processor;

import batch.domain.BoxOfDumplings;
import batch.domain.Dumpling;
import org.springframework.batch.item.ItemProcessor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.IntStream;

public class DeliveryProcessor implements ItemProcessor<BoxOfDumplings, Collection<Dumpling>> {
    @Override
    public Collection<Dumpling> process(BoxOfDumplings item) throws Exception {
        List<Dumpling> dumplings = new ArrayList<>();
        IntStream.rangeClosed(1, item.getDumplings())
                .forEach((int i) -> dumplings.add(new Dumpling(item.getType())));
        return dumplings;
    }
}
