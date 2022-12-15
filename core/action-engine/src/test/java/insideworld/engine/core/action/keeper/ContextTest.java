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

package insideworld.engine.core.action.keeper;

import insideworld.engine.core.action.ActionsTestTags;
import insideworld.engine.core.action.keeper.context.Context;
import insideworld.engine.core.action.keeper.tags.Tag;
import insideworld.engine.core.action.keeper.test.KeeperMatchers;
import insideworld.engine.core.common.injection.ObjectFactory;
import io.quarkus.test.junit.QuarkusTest;
import java.util.UUID;
import javax.inject.Inject;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Test context functionality.
 * @since 0.14.0
 */
@QuarkusTest
class ContextTest {

    /**
     * UUID string key for tests.
     */
    private static final String UUID_KEY = "uuid";

    /**
     * Dummy string key for tests.
     */
    private static final String DUMMY_KEY = "dummy";

    /**
     * Object factory.
     */
    private final ObjectFactory factory;

    /**
     * Default constructor.
     * @param factory Object factory
     */
    @Inject
    ContextTest(final ObjectFactory factory) {
        this.factory = factory;
    }

    /**
     * TC:
     * Test clone method with system and mandatory tags.
     */
    @Test
    final void testContext() {
        final Context context = this.factory.createObject(Context.class);
        context.put(TestKeeperTags.UUID, UUID.randomUUID());
        context.put(TestKeeperTags.DUMMY, new Object());
        context.put(ActionsTestTags.SYSTEM, new Object());
        context.put(ActionsTestTags.MANDATORY, new Object());
        final Context all = context.cloneContext();
        MatcherAssert.assertThat(
            "Wrong tags clone",
            all,
            Matchers.allOf(
                KeeperMatchers.contain(TestKeeperTags.DUMMY),
                KeeperMatchers.contain(TestKeeperTags.UUID),
                KeeperMatchers.contain(ActionsTestTags.MANDATORY),
                Matchers.not(KeeperMatchers.contain(ActionsTestTags.SYSTEM))
            )
        );
        final Context include = context.cloneContext(TestKeeperTags.UUID);
        MatcherAssert.assertThat(
            "Wrong tags clone with include",
            include,
            Matchers.allOf(
                Matchers.not(KeeperMatchers.contain(TestKeeperTags.DUMMY)),
                KeeperMatchers.contain(TestKeeperTags.UUID),
                KeeperMatchers.contain(ActionsTestTags.MANDATORY),
                Matchers.not(KeeperMatchers.contain(ActionsTestTags.SYSTEM))
            )
        );
        final Context strings = this.factory.createObject(Context.class);
        context.cloneToRecord(strings, TestKeeperTags.UUID.getTag());
        MatcherAssert.assertThat(
            "Wrong tags clone with clone to another record",
            strings,
            Matchers.allOf(
                Matchers.not(KeeperMatchers.contain(TestKeeperTags.DUMMY)),
                KeeperMatchers.contain(TestKeeperTags.UUID),
                KeeperMatchers.contain(ActionsTestTags.MANDATORY),
                Matchers.not(KeeperMatchers.contain(ActionsTestTags.SYSTEM))
            )
        );
    }

    /**
     * TC:
     * A lot of testes covered to put, get, contains and clone.
     * Test negative scenario and positive.
     */
    @SuppressWarnings("PMD.ExcessiveMethodLength")
    @Test
    final void testRecord() {
        final Record record = this.factory.createObject(Context.class);
        final UUID tag = UUID.randomUUID();
        final UUID string = UUID.randomUUID();
        record.put(TestKeeperTags.UUID, tag);
        record.put(ContextTest.UUID_KEY, string);
        record.put(TestKeeperTags.DUMMY, null);
        record.put(ContextTest.DUMMY_KEY, null);
        record.put((String) null, "SomeValue1");
        record.put((Tag<String>) null, "SomeValue2");
        record.put((Tag<String>) null, "SomeValue3", true);
        MatcherAssert.assertThat(
            "Tags in record is not equals to provided value",
            record,
            Matchers.allOf(
                KeeperMatchers.match(TestKeeperTags.UUID, Matchers.is(tag)),
                KeeperMatchers.match(ContextTest.UUID_KEY, Matchers.is(string)),
                KeeperMatchers.match(TestKeeperTags.DUMMY, Matchers.nullValue()),
                KeeperMatchers.match(ContextTest.DUMMY_KEY, Matchers.nullValue())
            )
        );
        MatcherAssert.assertThat(
            "Get by null key return not null",
            record.get((String) null),
            Matchers.nullValue()
        );
        MatcherAssert.assertThat(
            "Get by null tag return not null",
            record.get((Tag<?>) null),
            Matchers.nullValue()
        );
        MatcherAssert.assertThat(
            "Record contains key with null value",
            record.values().containsKey(null),
            Matchers.is(false)
        );
        Assertions.assertThrows(
            IllegalArgumentException.class,
            () -> record.put("uuid", UUID.randomUUID()),
            "Should be Illegal argument exception"
        );
        Assertions.assertThrows(
            IllegalArgumentException.class,
            () -> record.put(TestKeeperTags.UUID, UUID.randomUUID()),
            "Should be Illegal argument exception"
        );
        Assertions.assertThrows(
            IllegalArgumentException.class,
            () ->  record.put("uuid", UUID.randomUUID(), false),
            "Should be Illegal argument exception with using false"
        );
        Assertions.assertThrows(
            IllegalArgumentException.class,
            () ->  record.put(TestKeeperTags.UUID, UUID.randomUUID(), false),
            "Should be Illegal argument exception with using false"
        );
        MatcherAssert.assertThat(
            "Tags shouldn't change",
            record,
            Matchers.allOf(
                KeeperMatchers.match(ContextTest.UUID_KEY, Matchers.is(string)),
                KeeperMatchers.match(TestKeeperTags.UUID, Matchers.is(tag))
            )
        );
        final UUID onemore = UUID.randomUUID();
        Assertions.assertDoesNotThrow(
            () -> record.put(TestKeeperTags.UUID, onemore, true)
        );
        Assertions.assertDoesNotThrow(
            () -> record.put(ContextTest.UUID_KEY, onemore, true)
        );
        MatcherAssert.assertThat(
            "Tags should change",
            record,
            Matchers.allOf(
                KeeperMatchers.match(ContextTest.UUID_KEY, Matchers.is(onemore)),
                KeeperMatchers.match(TestKeeperTags.UUID, Matchers.is(onemore))
            )
        );
        MatcherAssert.assertThat(
            "Tags present in record not as expected",
            record,
            Matchers.allOf(
                KeeperMatchers.contain(TestKeeperTags.UUID),
                KeeperMatchers.contain(ContextTest.UUID_KEY),
                Matchers.not(KeeperMatchers.contain(TestKeeperTags.DUMMY)),
                Matchers.not(KeeperMatchers.contain(ContextTest.DUMMY_KEY))
            )
        );
        MatcherAssert.assertThat(
            "Null tag is present by key",
            record.contains((String) null),
            Matchers.is(false)
        );
        MatcherAssert.assertThat(
            "Null tag is present by tag",
            record.contains((Tag<?>) null),
            Matchers.is(false)
        );
        MatcherAssert.assertThat(
            "Null optional is present",
            record.getOptional(null).isEmpty(),
            Matchers.is(true)
        );
        MatcherAssert.assertThat(
            "Null tag is present by optional",
            record.getOptional(TestKeeperTags.DUMMY).isEmpty(),
            Matchers.is(true)
        );
        MatcherAssert.assertThat(
            "Exists tag is not present by optional",
            record.getOptional(TestKeeperTags.UUID).isPresent(),
            Matchers.is(true)
        );
        MatcherAssert.assertThat(
            "Not expected number of values in record",
            record.values(),
            Matchers.aMapWithSize(2)
        );
        final Record newrecord = this.factory.createObject(Context.class);
        record.cloneTag(TestKeeperTags.UUID, newrecord);
        record.cloneTag(TestKeeperTags.DUMMY, newrecord);
        record.cloneTag(null, newrecord);
        MatcherAssert.assertThat(
            "Not expected number of values in record",
            newrecord.values(),
            Matchers.aMapWithSize(1)
        );
        MatcherAssert.assertThat(
            "Not expected number of values in record",
            newrecord,
            KeeperMatchers.match(TestKeeperTags.UUID, Matchers.is(onemore))
        );
        Assertions.assertThrows(
            IllegalArgumentException.class,
            () -> record.cloneTag(TestKeeperTags.UUID, newrecord),
            "Excpetion expected here"
        );
    }
}
