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

import insideworld.engine.actions.keeper.context.Context;
import insideworld.engine.entities.StorageException;
import insideworld.engine.entities.mock.MockTags;
import insideworld.engine.entities.mock.entities.exception.MockWrongMethodEntity;
import insideworld.engine.entities.mock.entities.positive.MockEntity;
import insideworld.engine.entities.tags.StorageTags;
import insideworld.engine.injection.ObjectFactory;
import io.quarkus.test.junit.QuarkusTest;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import javax.inject.Inject;
import org.junit.jupiter.api.Test;

/**
 * Test exceptions handling for converter.
 * @since 0.14.0
 */
@QuarkusTest
class DtoConverterExceptionTest {
    /**
     * Converter for tests.
     */
    private final DtoConverter converter;

    /**
     * Object factory.
     */
    private final ObjectFactory factory;

    /**
     * Default constructor.
     * @param converter Converter for tests.
     * @param factory Object factory.
     */
    @Inject
    DtoConverterExceptionTest(
        final DtoConverter converter,
        final ObjectFactory factory) {
        this.converter = converter;
        this.factory = factory;
    }

    /**
     * TC: Try to convert entity with ID which not present in database.
     * ER: Should fail with message about null value from storage.
     */
    @Test
    final void testFailId() {
        final Context context = this.factory.createObject(Context.class);
        context.put(StorageTags.ID, 99_999L);
        context.put(MockTags.STRPRIM, "Value");
        context.put(MockTags.WRAPPRIMS, List.of(5L, 6L));
        context.put(MockTags.ONE_ID, 2L);
        context.put(MockTags.TWOS_IDS, List.of(7L, 8L, 9L));
        context.put(MockTags.DATE, new Date(1_500_000));
        context.put(MockTags.DATES, List.of(new Date(1_500_000), new Date(2_500_000)));
        boolean exception = false;
        try {
            this.converter.convert(context, MockEntity.class);
        } catch (final StorageException exp) {
            exception = exp.getMessage().contains("Can't read because storage return null");
        }
        assert exception;
    }

    /**
     * TC: Call converter for entity where getter and setter raise NPE.
     * ER: Message about failed read and write.
     */
    @Test
    final void wrongMethods() {
        final MockWrongMethodEntity entity = this.factory.createObject(MockWrongMethodEntity.class);
        boolean exception = false;
        try {
            this.converter.convert(entity);
        } catch (final StorageException exp) {
            exception = exp.getMessage().contains("Can't read field failed with type");
        }
        assert exception;
        exception = false;
        final Context context = this.factory.createObject(Context.class);
        try {
            this.converter.convert(context, MockWrongMethodEntity.class);
        } catch (final StorageException exp) {
            exception = exp.getMessage().contains("Can't write field failed with type");
        }
        assert exception;
    }

    /**
     * TC: Add date in string representation with wrong date value.
     * ER: Exception about can't parse date.
     */
    @Test
    final void failDateConvert() {
        final Context ctxsingle = this.factory.createObject(Context.class);
        ctxsingle.put(MockTags.DATE.getTag(), "qqqwertt123");
        boolean exception = false;
        try {
            this.converter.convert(ctxsingle, MockEntity.class);
        } catch (final StorageException exp) {
            exception = exp.getMessage().contains("Can't parse date");
        }
        assert exception;
        final Context ctxmulti = this.factory.createObject(Context.class);
        ctxmulti.put(MockTags.DATES.getTag(), Collections.singleton("qqqwertt123"));
        exception = false;
        try {
            this.converter.convert(ctxmulti, MockEntity.class);
        } catch (final StorageException exp) {
            exception = exp.getMessage().contains("Can't parse date");
        }
        assert exception;
    }
}
