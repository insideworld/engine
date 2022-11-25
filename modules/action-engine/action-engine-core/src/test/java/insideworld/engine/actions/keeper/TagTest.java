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

import insideworld.engine.actions.keeper.tags.SingleTag;
import io.quarkus.test.junit.QuarkusTest;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

/**
 * Just test TAG on equals and hashcode.
 * @since 0.14.0
 */
@QuarkusTest
class TagTest {

    /**
     * Test key.
     */
    private static final String TAG_KEY = "testKey";

    /**
     * Test tag one.
     */
    private static final SingleTag<Object> ONE = new SingleTag<>(TagTest.TAG_KEY);

    /**
     * Test tag two.
     */
    private static final SingleTag<Object> TWO = new SingleTag<>(TagTest.TAG_KEY);

    /**
     * Test tag another.
     */
    private static final SingleTag<Object> ANOTHER = new SingleTag<>("Another");

    /**
     * TC: Just compare different tags between each other.
     */
    @Test
    void test() {
        MatcherAssert.assertThat(TagTest.ONE, Matchers.is(TagTest.TWO));
        MatcherAssert.assertThat(TagTest.ONE, Matchers.not(TagTest.ANOTHER));
        MatcherAssert.assertThat(TagTest.ONE, Matchers.not(new Object()));
        MatcherAssert.assertThat(TagTest.ONE.hashCode(), Matchers.is(TagTest.TWO.hashCode()));
        MatcherAssert.assertThat(TagTest.ONE.hashCode(), Matchers.not(TagTest.ANOTHER.hashCode()));
        MatcherAssert.assertThat(TagTest.ONE.getTag(), Matchers.is(TagTest.TAG_KEY));
        MatcherAssert.assertThat(TagTest.TWO.getTag(), Matchers.is(TagTest.TAG_KEY));
    }
}
