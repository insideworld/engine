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

import insideworld.engine.core.action.ActionException;
import insideworld.engine.core.action.keeper.Record;
import insideworld.engine.core.action.keeper.context.Context;
import insideworld.engine.core.action.keeper.test.KeeperMatchers;
import insideworld.engine.core.data.core.StorageException;
import insideworld.engine.core.data.core.mock.InitMock;
import insideworld.engine.core.data.core.mock.MockTags;
import insideworld.engine.core.data.core.mock.entities.positive.MockEntity;
import insideworld.engine.core.data.core.mock.entities.positive.MockEntityImpl;
import insideworld.engine.core.data.core.tags.StorageTags;
import insideworld.engine.core.common.injection.ObjectFactory;
import io.quarkus.test.junit.QuarkusTest;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;
import javax.inject.Inject;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
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
     */
    @Test
    final void testToRecord() throws StorageException {
        final MockEntity entity = this.mock.createPrimary();
        final long id = entity.getId();
        final Record record = this.converter.convert(entity);
        MatcherAssert.assertThat(
            "Record has wrong values",
            record,
            Matchers.allOf(
                KeeperMatchers.match(StorageTags.ID, Matchers.is(id)),
                KeeperMatchers.match(MockTags.PRIM, Matchers.is(1337L)),
                KeeperMatchers.match(MockTags.WRAPPRIM, Matchers.is(1414L)),
                KeeperMatchers.match(MockTags.STRPRIM, Matchers.is("7331")),
                KeeperMatchers.match(MockTags.STRPRIMS, Matchers.hasSize(2)),
                KeeperMatchers.match(MockTags.WRAPPRIMS, Matchers.hasSize(4)),
                KeeperMatchers.match(MockTags.ONE_ID, Matchers.is(1L)),
                KeeperMatchers.match(MockTags.TWOS_IDS, Matchers.hasSize(3)),
                KeeperMatchers.match(MockTags.DATE, Matchers.is(new Date(1_000_000))),
                KeeperMatchers.match(MockTags.DATES, Matchers.hasSize(3)),
                Matchers.not(KeeperMatchers.contain(MockTags.TEST_NULL))
            )
        );
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
        this.mock.fillContext(context);
        MatcherAssert.assertThat(
            "Entity has wrong fields values",
            this.converter.convert(context, MockEntity.class),
            Matchers.allOf(
                Matchers.hasProperty("id", Matchers.is(0L)),
                Matchers.hasProperty("strprim", Matchers.is("Value")),
                Matchers.hasProperty("wrapprims", Matchers.iterableWithSize(2)),
                Matchers.hasProperty(
                    "one",
                    Matchers.hasProperty("id", Matchers.is(2L))
                ),
                Matchers.hasProperty("twos", Matchers.iterableWithSize(3)),
                Matchers.hasProperty("date", Matchers.is(new Date(1_500_000))),
                Matchers.hasProperty("dates", Matchers.iterableWithSize(2)),
                Matchers.hasProperty("testnull", Matchers.nullValue())
            )
        );
    }

    /**
     * Test that exists entity changed with new values.
     * @throws StorageException Can't cause.
     */
    @Test
    final void testToEntityExists() throws StorageException {
        final MockEntity entity = this.mock.createPrimary();
        final long id = entity.getId();
        final Context context = this.factory.createObject(Context.class);
        context.put(StorageTags.ID, entity.getId());
        this.mock.fillContext(context);
        MatcherAssert.assertThat(
            "Entity has wrong fields values",
            this.converter.convert(context, MockEntity.class),
            Matchers.allOf(
                Matchers.hasProperty("id", Matchers.is(id)),
                Matchers.hasProperty("strprim", Matchers.is("Value")),
                Matchers.hasProperty("wrapprims", Matchers.iterableWithSize(2)),
                Matchers.hasProperty(
                    "one",
                    Matchers.hasProperty("id", Matchers.is(2L))
                ),
                Matchers.hasProperty("twos", Matchers.iterableWithSize(3)),
                Matchers.hasProperty("date", Matchers.is(new Date(1_500_000))),
                Matchers.hasProperty("dates", Matchers.iterableWithSize(2)),
                Matchers.hasProperty("testnull", Matchers.nullValue())
            )
        );
    }

    /**
     * TC: Test string date to converter.
     * Set in date and dates tag string representation with the same mask as in mappers
     * ER: Created date should equal with dates from entity.
     * @throws StorageException Can't cause.
     */
    @Test
    final void testStringDate() throws StorageException {
        final SimpleDateFormat format =
            new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSX", Locale.getDefault());
        final Date date = new Date();
        final String str = format.format(date);
        final Context context = this.factory.createObject(Context.class);
        context.put(MockTags.DATE.getTag(), str);
        context.put(MockTags.DATES.getTag(), Collections.singleton(str));
        final MockEntity entity = this.converter.convert(context, MockEntity.class);
        assert entity.getDate().equals(date);
        assert entity.getDates().iterator().next().equals(date);
        MatcherAssert.assertThat(
            "Dates has wrong value",
            this.converter.convert(context, MockEntity.class),
            Matchers.allOf(
                Matchers.hasProperty("date", Matchers.is(date)),
                Matchers.hasProperty("dates", Matchers.hasItem(date))
            )
        );
    }

    /**
     * TC: Test convert entity with null values.
     * ER: All field for primitive and dates should null
     *  and collection should be empty, but no null.
     * @throws StorageException Shouldn't throw.
     */
    @Test
    final void nullValueContext() throws StorageException {
        final Context context = this.factory.createObject(Context.class);
        context.put(MockTags.STRPRIM, null);
        context.put(MockTags.WRAPPRIMS, null);
        context.put(MockTags.ONE_ID, null);
        context.put(MockTags.TWOS_IDS, null);
        context.put(MockTags.DATE, null);
        context.put(MockTags.DATES, null);
        MatcherAssert.assertThat(
            "Entity has wrong fields values for nulls",
            this.converter.convert(context, MockEntity.class),
            Matchers.allOf(
                Matchers.hasProperty("strprim", Matchers.nullValue()),
                Matchers.hasProperty("wrapprims", Matchers.iterableWithSize(0)),
                Matchers.hasProperty("one", Matchers.nullValue()),
                Matchers.hasProperty("twos", Matchers.iterableWithSize(0)),
                Matchers.hasProperty("date", Matchers.nullValue()),
                Matchers.hasProperty("dates", Matchers.iterableWithSize(0)),
                Matchers.hasProperty("testnull", Matchers.nullValue())
            )
        );
    }

    @Test
    final void nullValueEntity() throws StorageException {
        final MockEntity entity = new MockEntityImpl();
        final Record record = this.converter.convert(entity);
        MatcherAssert.assertThat(
            "Record has wrong values",
            record,
            Matchers.allOf(
                Matchers.not(KeeperMatchers.contain(StorageTags.ID)),
                Matchers.not(KeeperMatchers.contain(MockTags.PRIM)),
                Matchers.not(KeeperMatchers.contain(MockTags.WRAPPRIM)),
                Matchers.not(KeeperMatchers.contain(MockTags.STRPRIM)),
                Matchers.not(KeeperMatchers.contain(MockTags.STRPRIMS)),
                Matchers.not(KeeperMatchers.contain(MockTags.WRAPPRIMS)),
                Matchers.not(KeeperMatchers.contain(MockTags.ONE_ID)),
                Matchers.not(KeeperMatchers.contain(MockTags.TWOS_IDS)),
                Matchers.not(KeeperMatchers.contain(MockTags.DATE)),
                Matchers.not(KeeperMatchers.contain(MockTags.DATES)),
                Matchers.not(KeeperMatchers.contain(MockTags.TEST_NULL))
            )
        );
    }

}
