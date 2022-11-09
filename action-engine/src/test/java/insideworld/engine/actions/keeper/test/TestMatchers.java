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

package insideworld.engine.actions.keeper.test;

import insideworld.engine.actions.keeper.TestKeeperTags;
import insideworld.engine.actions.keeper.context.Context;
import insideworld.engine.injection.ObjectFactory;
import io.quarkus.test.junit.QuarkusTest;
import java.util.UUID;
import javax.inject.Inject;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.hamcrest.StringDescription;
import org.junit.jupiter.api.Test;

/**
 * Test matchers.
 *
 * @since 0.14.0
 */
@QuarkusTest
class TestMatchers {

    /**
     * Object factory.
     */
    private final ObjectFactory factory;

    /**
     * Default constructor.
     *
     * @param factory Object factory.
     */
    @Inject
    TestMatchers(final ObjectFactory factory) {
        this.factory = factory;
    }

    /**
     * TC:
     * Test matchers by tag and string key for tag UUID.
     * Test message.
     * ER:
     * If tag present matcher should return true.
     */
    @Test
    final void testContains() {
        final Context context = this.factory.createObject(Context.class);
        context.put(TestKeeperTags.UUID, UUID.randomUUID());
        final var exists = KeeperMatchers.recordContain(TestKeeperTags.UUID);
        MatcherAssert.assertThat(
            "Should return true because tag exists",
            exists.matchesSafely(context),
            Matchers.is(true)
        );
        final var not = KeeperMatchers.recordContain(TestKeeperTags.DUMMY);
        MatcherAssert.assertThat(
            "Should return false because tag not exists",
            not.matchesSafely(context),
            Matchers.is(false)
        );
        final var existss = KeeperMatchers.recordContain(
            TestKeeperTags.UUID.getTag()
        );
        MatcherAssert.assertThat(
            "Should return true because tag exists",
            existss.matchesSafely(context),
            Matchers.is(true)
        );
        final var nots = KeeperMatchers.recordContain(
            TestKeeperTags.DUMMY.getTag()
        );
        MatcherAssert.assertThat(
            "Should return false because tag not exists",
            nots.matchesSafely(context),
            Matchers.is(false)
        );
        final StringDescription description = new StringDescription();
        exists.describeTo(description);
        MatcherAssert.assertThat(
            "Wrong message description",
            description.toString(),
            Matchers.equalTo(
                String.format(
                    "Tag %s is not found in record",
                    TestKeeperTags.UUID.getTag()
                )
            )
        );
    }

    /**
     * TC:
     * Test matchers by tag and string key for tag UUID.
     * Test message.
     * ER:
     * If tag present and equals matcher should return true.
     */
    @Test
    final void testEquals() {
        final Context context = this.factory.createObject(Context.class);
        final UUID uuid = UUID.randomUUID();
        context.put(TestKeeperTags.UUID, uuid);
        final var equals = KeeperMatchers.recordEquals(
            TestKeeperTags.UUID,
            uuid
        );
        MatcherAssert.assertThat(
            "Should return true because tag exists and value equals",
            equals.matchesSafely(context),
            Matchers.is(true)
        );
        final var equalss = KeeperMatchers.recordEquals(
            TestKeeperTags.UUID.getTag(),
            uuid
        );
        MatcherAssert.assertThat(
            "Should return true because key exists and value equals",
            equalss.matchesSafely(context),
            Matchers.is(true)
        );
        context.put(TestKeeperTags.UUID, UUID.randomUUID(), true);
        MatcherAssert.assertThat(
            "Should return false because tag exists and value not equals",
            equals.matchesSafely(context),
            Matchers.is(false)
        );
        final var nul = KeeperMatchers.recordEquals(
            TestKeeperTags.UUID,
            uuid
        );
        MatcherAssert.assertThat(
            "Should return false because tag not exists",
            nul.matchesSafely(this.factory.createObject(Context.class)),
            Matchers.is(false)
        );
        final StringDescription description = new StringDescription();
        equals.describeTo(description);
        MatcherAssert.assertThat(
            "Wrong message description",
            description.toString(),
            Matchers.equalTo(
                String.format(
                    "Expected value for tag %s is <%s>",
                    TestKeeperTags.UUID.getTag(),
                    uuid
                )
            )
        );
    }
}
