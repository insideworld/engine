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
import insideworld.engine.entities.mock.MockTags;
import insideworld.engine.entities.mock.entities.exclude.MockExcludeEntity;
import insideworld.engine.entities.tags.StorageTags;
import insideworld.engine.injection.ObjectFactory;
import io.quarkus.test.junit.QuarkusTest;
import javax.inject.Inject;
import org.junit.jupiter.api.Test;

/**
 * Test scenario for converter where some method is missed.
 * @since 0.14.0
 */
@QuarkusTest
class DtoConverterExcludedTest {

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
    DtoConverterExcludedTest(
        final DtoConverter converter,
        final ObjectFactory factory) {
        this.converter = converter;
        this.factory = factory;
    }

    /**
     * TC: Check that in record propagate only fields with getter method.
     * ER: In record should present only ID and NO_SET tag.
     * @throws ActionException Can't cause.
     */
    @Test
    final void toRecord() throws ActionException {
        final MockExcludeEntity entity = this.factory.createObject(MockExcludeEntity.class);
        final Record record = this.converter.convert(entity);
        assert record.contains(StorageTags.ID);
        assert record.contains(MockTags.NO_SET);
        assert !record.contains(MockTags.NO_GET);
        assert !record.contains(MockTags.NO_METHODS);
    }

    /**
     * TC: Check that in entity propagate only field with setter method.
     * ER: In entity should change only NO_GET field.
     * @throws ActionException Can't cause.
     */
    @Test
    final void toEntity() throws ActionException {
        final Context context = this.factory.createObject(Context.class);
        context.put(MockTags.NO_SET, "NewValue1");
        context.put(MockTags.NO_GET, "NewValue2");
        context.put(MockTags.NO_METHODS, "NewValue3");
        final MockExcludeEntity entity = this.converter.convert(context, MockExcludeEntity.class);
        assert entity.check("NewValue2");
    }

}
