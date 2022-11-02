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

package insideworld.engine.actions.keeper;

import insideworld.engine.actions.ActionsTestTags;
import insideworld.engine.actions.keeper.context.Context;
import insideworld.engine.actions.keeper.tags.Tag;
import insideworld.engine.injection.ObjectFactory;
import io.quarkus.test.junit.QuarkusTest;
import java.util.UUID;
import javax.inject.Inject;
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
        assert all.contains(TestKeeperTags.DUMMY);
        assert all.contains(TestKeeperTags.UUID);
        assert all.contains(ActionsTestTags.MANDATORY);
        assert !all.contains(ActionsTestTags.SYSTEM);
        final Context include = context.cloneContext(TestKeeperTags.UUID);
        assert !include.contains(TestKeeperTags.DUMMY);
        assert include.contains(TestKeeperTags.UUID);
        assert include.contains(ActionsTestTags.MANDATORY);
        assert !include.contains(ActionsTestTags.SYSTEM);
        final Context strings = this.factory.createObject(Context.class);
        context.cloneToRecord(strings, TestKeeperTags.UUID.getTag());
        assert !strings.contains(TestKeeperTags.DUMMY);
        assert strings.contains(TestKeeperTags.UUID);
        assert strings.contains(ActionsTestTags.MANDATORY);
        assert !strings.contains(ActionsTestTags.SYSTEM);
    }

    /**
     * TC:
     * A lot of testes covered to put, get, contains and clone.
     * Test negative scenario and positive.
     */
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
        assert record.get(TestKeeperTags.UUID).equals(tag);
        assert record.get(ContextTest.UUID_KEY).equals(string);
        assert record.get(TestKeeperTags.DUMMY) == null;
        assert record.get(ContextTest.DUMMY_KEY) == null;
        assert record.get((String) null) == null;
        assert record.get((Tag<?>) null) == null;
        assert !record.values().containsKey(null);
        assert this.exception(() -> record.put("uuid", UUID.randomUUID()));
        assert record.get(ContextTest.UUID_KEY).equals(string);
        assert this.exception(() -> record.put(TestKeeperTags.UUID, UUID.randomUUID()));
        assert record.get(TestKeeperTags.UUID).equals(tag);
        assert this.exception(() -> record.put("uuid", UUID.randomUUID(), false));
        assert record.get(ContextTest.UUID_KEY).equals(string);
        assert this.exception(
            () -> record.put(TestKeeperTags.UUID, UUID.randomUUID(), false)
        );
        assert record.get(TestKeeperTags.UUID).equals(tag);
        final UUID onemore = UUID.randomUUID();
        assert !this.exception(
            () -> record.put(TestKeeperTags.UUID, onemore, true)
        );
        assert record.get(TestKeeperTags.UUID).equals(onemore);
        assert !this.exception(
            () -> record.put(ContextTest.UUID_KEY, onemore, true)
        );
        assert record.get(ContextTest.UUID_KEY).equals(onemore);
        assert record.contains(TestKeeperTags.UUID);
        assert record.contains(ContextTest.UUID_KEY);
        assert !record.contains(TestKeeperTags.DUMMY);
        assert !record.contains(ContextTest.DUMMY_KEY);
        assert !record.contains((String) null);
        assert !record.contains((Tag<?>) null);
        assert record.getOptional(null).isEmpty();
        assert record.getOptional(TestKeeperTags.DUMMY).isEmpty();
        assert record.getOptional(TestKeeperTags.UUID).isPresent();
        assert record.values().size() == 2;
        final Record newrecord = this.factory.createObject(Context.class);
        record.cloneTag(TestKeeperTags.UUID, newrecord);
        record.cloneTag(TestKeeperTags.DUMMY, newrecord);
        record.cloneTag(null, newrecord);
        assert newrecord.values().size() == 1;
        assert newrecord.get(TestKeeperTags.UUID).equals(onemore);
        assert this.exception(() -> record.cloneTag(TestKeeperTags.UUID, newrecord));
    }

    /**
     * Test exception on contains key.
     * @param call Call put function.
     * @return If exception raised - true, else - false.
     */
    private boolean exception(final Call call) {
        boolean exception = false;
        try {
            call.call();
        } catch (final IllegalArgumentException exp) {
            if ("Record already contains this key!".equals(exp.getMessage())) {
                exception = true;
            }
        }
        return exception;
    }

    /**
     * Just predicate for exception call.
     * @since 0.14.0
     */
    private interface Call {

        /**
         * Just call a function.
         */
        void call();
    }

}
