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
import insideworld.engine.actions.keeper.context.Context;
import insideworld.engine.entities.mock.MockTags;
import insideworld.engine.entities.mock.entities.positive.MockEntity;
import insideworld.engine.entities.tags.StorageTags;
import insideworld.engine.injection.ObjectFactory;
import io.quarkus.test.junit.QuarkusTest;
import java.util.Date;
import java.util.List;
import javax.inject.Inject;
import org.junit.jupiter.api.Test;

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

    @Test
    final void testFailId() throws ActionException {
        final Context context = this.factory.createObject(Context.class);
        context.put(StorageTags.ID, 99999L);
        context.put(MockTags.VALUE, "Value");
        context.put(MockTags.VALUES, List.of(5L, 6L));
        context.put(MockTags.ONE_ID, 2L);
        context.put(MockTags.TWOS_IDS, List.of(7L, 8L, 9L));
        context.put(MockTags.DATE, new Date(1_500_000));
        context.put(MockTags.DATES, List.of(new Date(1_500_000), new Date(2_500_000)));
        this.converter.convert(context, MockEntity.class);
    }
}
