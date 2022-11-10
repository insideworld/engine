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

import insideworld.engine.actions.keeper.context.Context;
import insideworld.engine.actions.keeper.output.Output;
import insideworld.engine.actions.keeper.test.KeeperMatchers;
import insideworld.engine.injection.ObjectFactory;
import io.quarkus.test.junit.QuarkusTest;
import java.util.UUID;
import javax.inject.Inject;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

/**
 * Output tests.
 *
 * @since 0.14.0
 */
@QuarkusTest
class OutputTest {

    /**
     * Object factory.
     */
    private final ObjectFactory factory;

    /**
     * Default constructor.
     *
     * @param factory Object factory
     */
    @Inject
    OutputTest(final ObjectFactory factory) {
        this.factory = factory;
    }

    /**
     * TC: Create 3 record. One with the dummy tag, one with alias, one with custom record.
     * ER: Should create 2 record and add one.
     * One with the dummy tag, one with alias, one with custom record.
     */
    @Test
    final void testCreateRecord() {
        final Output output = this.factory.createObject(Output.class);
        final Record record = output.createRecord();
        output.createRecord("SomeAlias");
        record.put(TestKeeperTags.DUMMY, new Object());
        final Context context = this.factory.createObject(Context.class);
        context.put(TestKeeperTags.UUID, UUID.randomUUID());
        output.add(context);
        MatcherAssert.assertThat(
            "Output has wrong values",
            output,
            Matchers
                .both(
                    Matchers.<Record>iterableWithSize(3)
                )
                .and(
                    Matchers.containsInRelativeOrder(
                        KeeperMatchers.contain(TestKeeperTags.DUMMY),
                        KeeperMatchers.match(KeeperTags.ALIAS, Matchers.is("SomeAlias")),
                        KeeperMatchers.contain(TestKeeperTags.UUID)
                    )
                )
        );
    }

}
