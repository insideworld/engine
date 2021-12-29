package insideworld.engine.integration.tests.entities.convertor;

import com.google.common.collect.ImmutableList;
import insideworld.engine.actions.ActionException;
import insideworld.engine.actions.keeper.Record;
import insideworld.engine.actions.keeper.context.Context;
import insideworld.engine.entities.converter.EntityConverter;
import insideworld.engine.entities.storages.StorageException;
import insideworld.engine.injection.ObjectFactory;
import insideworld.engine.integration.entities.convertor.JpaTestMain;
import insideworld.engine.integration.entities.convertor.MainStorage;
import io.quarkus.test.junit.QuarkusTest;

import javax.inject.Inject;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@QuarkusTest
public class TestConvertor {

    private static final Logger LOGGER = LoggerFactory.getLogger(TestConvertor.class);

    private final EntityConverter converter;
    private final ObjectFactory factory;
    private final MainStorage storage;

    @Inject
    public TestConvertor(final EntityConverter converter,
                         final ObjectFactory factory,
                         final MainStorage storage) {
        this.converter = converter;
        this.factory = factory;
        this.storage = storage;
    }

    @Test
    public void test() throws ActionException, StorageException {
        final JpaTestMain entity = this.storage.read(1);
        final Record convert = this.converter.convert(entity);
        LOGGER.trace("Test");
        final Context context = factory.createObject(Context.class);
        context.put("id", 1L);
        context.put("name", "entity");
        context.put("description", "desctiption of entity");
        context.put("arraysIds", ImmutableList.of(1L, 3L, 4L, 10L));
        final JpaTestMain qwe = this.converter.convert(context, JpaTestMain.class);
        LOGGER.trace(convert.toString());
    }
}