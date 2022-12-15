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

package insideworld.engine.core.data.core.converter.dto;

import insideworld.engine.core.action.keeper.context.Context;
import insideworld.engine.core.data.core.StorageException;
import insideworld.engine.core.data.core.mock.MockTags;
import insideworld.engine.core.data.core.mock.entities.exception.MockWrongMethodEntity;
import insideworld.engine.core.data.core.mock.entities.positive.MockEntity;
import insideworld.engine.core.data.core.tags.StorageTags;
import insideworld.engine.core.common.injection.ObjectFactory;
import insideworld.engine.core.common.matchers.exception.ExceptionMatchers;
import io.quarkus.test.junit.QuarkusTest;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import javax.inject.Inject;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Test exceptions handling for converter.
 *
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
     *
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
        final StorageException exception = Assertions.assertThrows(
            StorageException.class,
            () -> this.converter.convert(context, MockEntity.class),
            "Should raise an exception"
        );
        MatcherAssert.assertThat(
            "Exception has incorrect message",
            exception,
            ExceptionMatchers.messageMatcher(
                0,
                Matchers.containsString("Can't read because storage return null")
            )
        );
    }

    /**
     * TC: Call converter for entity where getter and setter raise NPE.
     * ER: Message about failed read and write.
     */
    @Test
    final void wrongMethods() {
        final MockWrongMethodEntity entity = this.factory.createObject(MockWrongMethodEntity.class);
        final StorageException read = Assertions.assertThrows(
            StorageException.class,
            () -> this.converter.convert(entity),
            "Should raise an exception"
        );
        MatcherAssert.assertThat(
            "Exception has incorrect message",
            read,
            ExceptionMatchers.messageMatcher(
                0,
                Matchers.containsString("Can't read field failed with type")
            )
        );
        final StorageException write = Assertions.assertThrows(
            StorageException.class,
            () -> this.converter.convert(
                this.factory.createObject(Context.class),
                MockWrongMethodEntity.class
            ),
            "Should raise an exception"
        );
        MatcherAssert.assertThat(
            "Exception has incorrect message",
            write,
            ExceptionMatchers.messageMatcher(
                0,
                Matchers.containsString("Can't write field failed with type")
            )
        );
    }

    /**
     * TC: Add date in string representation with wrong date value.
     * ER: Exception about can't parse date.
     */
    @Test
    final void failDateConvert() {
        final Context ctxsingle = this.factory.createObject(Context.class);
        ctxsingle.put(MockTags.DATE.getTag(), "qqqwertt123");
        MatcherAssert.assertThat(
            "Exception is wrong for date parse",
            () -> this.converter.convert(ctxsingle, MockEntity.class),
            ExceptionMatchers.catchException(
                StorageException.class,
                ExceptionMatchers.messageMatcher(
                    0,
                    Matchers.containsString("Can't parse date")
                )
            )
        );
        final Context ctxmulti = this.factory.createObject(Context.class);
        ctxmulti.put(MockTags.DATES.getTag(), Collections.singleton("qqqwertt123"));
        MatcherAssert.assertThat(
            "Exception is wrong for date parse",
            () -> this.converter.convert(ctxmulti, MockEntity.class),
            ExceptionMatchers.catchException(
                StorageException.class,
                ExceptionMatchers.messageMatcher(
                    0,
                    Matchers.containsString("Can't parse date")
                )
            )
        );
    }
}
