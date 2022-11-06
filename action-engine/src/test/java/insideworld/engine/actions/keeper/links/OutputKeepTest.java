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

package insideworld.engine.actions.keeper.links;

import insideworld.engine.actions.ActionException;
import insideworld.engine.actions.keeper.Record;
import insideworld.engine.actions.keeper.TestKeeperTags;
import insideworld.engine.actions.keeper.output.Output;
import insideworld.engine.injection.ObjectFactory;
import io.quarkus.test.junit.QuarkusTest;
import java.util.Iterator;
import javax.inject.Inject;
import org.junit.jupiter.api.Test;

/**
 * Test output keep link.
 * @since 0.14.0
 */
@QuarkusTest
class OutputKeepTest {

    /**
     * Object factory.
     */
    private final ObjectFactory factory;

    /**
     * Default constructor.
     * @param factory Object factory.
     */
    @Inject
    OutputKeepTest(final ObjectFactory factory) {
        this.factory = factory;
    }

    /**
     * TC: Create 3 record in output with different tags.
     * ER: Should stay only 2 tags or 1 if it not exists originally.
     * @throws ActionException Shouldn't raise.
     */
    @Test
    final void test() throws ActionException {
        final Output output = this.factory.createObject(Output.class);
        final Record one = output.createRecord();
        one.put(TestKeeperTags.DUMMY, new Object());
        one.put(TestKeeperTags.ONE, new Object());
        one.put(TestKeeperTags.TWO, new Object());
        final Record two = output.createRecord();
        two.put(TestKeeperTags.DUMMY, new Object());
        two.put(TestKeeperTags.ONE, new Object());
        final Record three = output.createRecord();
        three.put(TestKeeperTags.DUMMY, new Object());
        final OutputKeep link = this.factory.createObject(OutputKeep.class);
        link.add(TestKeeperTags.DUMMY, TestKeeperTags.ONE);
        link.process(null, output);
        this.checkOutput(output);
    }

    /**
     * Check what output records contains keeping tags.
     * @param output Output.
     */
    private void checkOutput(final Output output) {
        final Iterator<Record> iterator = output.iterator();
        final Record one = iterator.next();
        assert one.contains(TestKeeperTags.DUMMY);
        assert one.contains(TestKeeperTags.ONE);
        assert !one.contains(TestKeeperTags.TWO);
        final Record two = iterator.next();
        assert two.contains(TestKeeperTags.DUMMY);
        assert two.contains(TestKeeperTags.ONE);
        assert !two.contains(TestKeeperTags.TWO);
        final Record three = iterator.next();
        assert three.contains(TestKeeperTags.DUMMY);
        assert !three.contains(TestKeeperTags.ONE);
        assert !three.contains(TestKeeperTags.TWO);
    }

}
