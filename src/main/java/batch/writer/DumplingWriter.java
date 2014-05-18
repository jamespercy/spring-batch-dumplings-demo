package batch.writer;

import batch.domain.Dumpling;
import org.springframework.batch.item.ItemWriter;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.Collection;
import java.util.List;

public class DumplingWriter implements ItemWriter<Collection<Dumpling>> {

    private final MongoTemplate mongoTemplate;

    public DumplingWriter(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public void write(List<? extends Collection<Dumpling>> dumplingsGroups) throws Exception {
        dumplingsGroups.forEach(dumplings -> dumplings.forEach(mongoTemplate::save));
    }
}
