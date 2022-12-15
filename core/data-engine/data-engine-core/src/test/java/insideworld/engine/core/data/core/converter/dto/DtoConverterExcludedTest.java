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
import insideworld.engine.core.action.keeper.test.KeeperMatchers;
import insideworld.engine.core.data.core.StorageException;
import insideworld.engine.core.data.core.mock.MockTags;
import insideworld.engine.core.data.core.mock.entities.exclude.MockExcludeEntity;
import insideworld.engine.core.data.core.tags.StorageTags;
import insideworld.engine.core.common.injection.ObjectFactory;
import insideworld.engine.core.common.matchers.object.ObjectMappers;
import io.quarkus.test.junit.QuarkusTest;
import java.util.List;
import javax.inject.Inject;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

/**
 * Test scenario for converter where some method is missed.
 *
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
     *
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
     *
     * @throws StorageException Can't cause.
     */
    @Test
    final void toRecord() throws StorageException {
        final MockExcludeEntity entity = this.factory.createObject(MockExcludeEntity.class);
        MatcherAssert.assertThat(
            "Record has incorrect tags",
            this.converter.convert(entity),
            Matchers.allOf(
                KeeperMatchers.contain(StorageTags.ID),
                KeeperMatchers.contain(MockTags.NO_SET),
                Matchers.not(KeeperMatchers.contain(MockTags.NO_GET)),
                Matchers.not(KeeperMatchers.contain(MockTags.NO_METHODS)),
                Matchers.not(KeeperMatchers.contain(MockTags.IGNORE)),
                Matchers.not(KeeperMatchers.contain(MockTags.OBJECTS))
            )
        );
    }

    /**
     * TC: Check that in entity propagate only field with setter method.
     * ER: In entity should change only NO_GET field.
     *
     * @throws StorageException Can't cause.
     */
    @Test
    final void toEntity() throws StorageException {
        final Context context = this.factory.createObject(Context.class);
        context.put(MockTags.NO_SET, "NewValue1");
        context.put(MockTags.NO_GET, "NewValue2");
        context.put(MockTags.NO_METHODS, "NewValue3");
        context.put(MockTags.IGNORE, "NewValue4");
        context.put(MockTags.OBJECTS, List.of(new Object()));
        final MockExcludeEntity entity = this.converter.convert(context, MockExcludeEntity.class);
        MatcherAssert.assertThat(
            "Fields values are not as expected",
            entity,
            Matchers.allOf(
                ObjectMappers.fieldMatcher("objects", Matchers.iterableWithSize(2)),
                ObjectMappers.fieldMatcher("noset", Matchers.is("testnoset")),
                ObjectMappers.fieldMatcher("ignore", Matchers.is("ignore")),
                ObjectMappers.fieldMatcher("nomethods", Matchers.is("testnomethods")),
                ObjectMappers.fieldMatcher("noget", Matchers.is("NewValue2"))
            )
        );
    }

}
