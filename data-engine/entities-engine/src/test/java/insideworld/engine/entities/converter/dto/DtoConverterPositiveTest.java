/*
 * Copyright (c) 2022 Anton Eliseev
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
 * associated documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute,
 * sublicense, and/or sell copies of the Software, and to permit persons to whom the Software
 * is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or
 * substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT
 * NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package insideworld.engine.entities.converter.dto;

import insideworld.engine.actions.ActionException;
import insideworld.engine.actions.keeper.Record;
import insideworld.engine.actions.keeper.context.Context;
import insideworld.engine.entities.mock.InitMock;
import insideworld.engine.entities.mock.MockTags;
import insideworld.engine.entities.mock.entities.positive.MockEntity;
import insideworld.engine.entities.StorageException;
import insideworld.engine.entities.tags.StorageTags;
import insideworld.engine.injection.ObjectFactory;
import io.quarkus.test.junit.QuarkusTest;
import java.util.Date;
import java.util.List;
import javax.inject.Inject;
import org.junit.jupiter.api.Test;

/**
 * DTO tests.
 *
 * @since 0.14.0
 */
@QuarkusTest
class DtoConverterPositiveTest {

    /**
     * Converter for tests.
     */
    private final DtoConverter converter;

    /**
     * Object factory.
     */
    private final ObjectFactory factory;

    /**
     * Storage for primary entity.
     */
    private final InitMock mock;

    /**
     * Default constructor.
     * @param converter Converter for tests.
     * @param factory Object factory.
     * @param mock Mock for entity.
     */
    @Inject
    DtoConverterPositiveTest(
        final DtoConverter converter,
        final ObjectFactory factory,
        final InitMock mock) {
        this.converter = converter;
        this.factory = factory;
        this.mock = mock;
    }

    /**
     * TC: Check that mock entity keeping in storage was converted to DTO.
     * ER: Record with propagated field from entity.
     * @throws StorageException Can't cause.
     * @throws ActionException Can't cause.
     */
    @Test
    final void testToRecord() throws StorageException, ActionException {
        final MockEntity entity = this.mock.createPrimary();
        final long id = entity.getId();
        final Record record = this.converter.convert(entity);
        assert record.contains(StorageTags.ID) && record.get(StorageTags.ID).equals(id);
        assert record.contains(MockTags.VALUE) && "Some value".equals(record.get(MockTags.VALUE));
        assert record.contains(MockTags.VALUES) && record.get(MockTags.VALUES).size() == 5;
        assert record.contains(MockTags.ONE_ID) && record.get(MockTags.ONE_ID) == 1L;
        assert record.contains(MockTags.TWOS_IDS) && record.get(MockTags.TWOS_IDS).size() == 3;
        assert record.contains(MockTags.DATE) && record.get(MockTags.DATE).getTime() == 1_000_000;
        assert record.contains(MockTags.DATES) && record.get(MockTags.DATES).size() == 3;
        assert !record.contains(MockTags.TEST_NULL);
    }

    /**
     * TC: Create a DTO with values and try to create a new entity.
     * ER: Should be created entity with filled fields.
     * @throws StorageException Can't cause.
     * @throws ActionException Can't cause.
     */
    @Test
    final void testToEntityNew() throws StorageException, ActionException {
        final Context context = this.factory.createObject(Context.class);
        context.put(MockTags.VALUE, "Value");
        context.put(MockTags.VALUES, List.of(5L, 6L));
        context.put(MockTags.ONE_ID, 2L);
        context.put(MockTags.TWOS_IDS, List.of(7L, 8L, 9L));
        context.put(MockTags.DATE, new Date(1_500_000));
        context.put(MockTags.DATES, List.of(new Date(1_500_000), new Date(2_500_000)));
        final MockEntity entity = this.converter.convert(context, MockEntity.class);
        assert entity.getId() == 0;
        assert "Value".equals(entity.getValue());
        assert entity.getValues().size() == 2;
        assert entity.getOne().getId() == 2;
        assert entity.getTwos().size() == 3;
        assert entity.getDate().getTime() == 1_500_000;
        assert entity.getDates().size() == 2;
        assert entity.getTestnull() == null;
    }

    /**
     * Test that exists entity changed with new values.
     * @throws ActionException
     */
    @Test
    final void testToEntityExists() throws ActionException, StorageException {
        final MockEntity entity = this.mock.createPrimary();
        final long id = entity.getId();
        final Context context = this.factory.createObject(Context.class);
        context.put(StorageTags.ID, entity.getId());
        context.put(MockTags.VALUE, "Value");
        context.put(MockTags.VALUES, List.of(5L, 6L));
        context.put(MockTags.ONE_ID, 2L);
        context.put(MockTags.TWOS_IDS, List.of(7L, 8L, 9L));
        context.put(MockTags.DATE, new Date(1_500_000));
        context.put(MockTags.DATES, List.of(new Date(1_500_000), new Date(2_500_000)));
        final MockEntity convert = this.converter.convert(context, MockEntity.class);
        assert convert.getId() == id;
        assert "Value".equals(convert.getValue());
        assert convert.getValues().size() == 2;
        assert convert.getOne().getId() == 2;
        assert convert.getTwos().size() == 3;
        assert convert.getDate().getTime() == 1_500_000;
        assert convert.getDates().size() == 2;
        assert convert.getTestnull() == null;
    }
}
