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

package insideworld.engine.entities.extractor;

import insideworld.engine.actions.chain.LinkException;
import insideworld.engine.actions.keeper.Record;
import insideworld.engine.actions.keeper.context.Context;
import insideworld.engine.actions.keeper.output.Output;
import insideworld.engine.actions.keeper.test.KeeperMatchers;
import insideworld.engine.entities.tags.StorageTags;
import insideworld.engine.injection.ObjectFactory;
import insideworld.engine.matchers.exception.ExceptionMatchers;
import io.quarkus.test.junit.QuarkusTest;
import javax.inject.Inject;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

/**
 * Test extractor and executor links.
 *
 * @since 0.14.0
 */
@QuarkusTest
class ExtractorTest {

    /**
     * Test schema name.
     */
    private static final String SCHEMA = "test.schema";

    /**
     * Extractor instance.
     */
    private final ExtractorLink extractor;

    /**
     * Executor instance.
     */
    private final ExecutorLink executor;

    /**
     * Object factory.
     */
    private final ObjectFactory factory;

    /**
     * Default constructor.
     *
     * @param extractor Extractor instance.
     * @param executor Executor instance.
     * @param factory Object factory.
     */
    @Inject
    ExtractorTest(
        final ExtractorLink extractor,
        final ExecutorLink executor,
        final ObjectFactory factory
    ) {
        this.extractor = extractor;
        this.executor = executor;
        this.factory = factory;
    }

    /**
     * TC: Execute extractor for different cases.
     * 1. Forgotten setup of schema.
     * 2. Extractor create on record in output.
     * 3. Extractor thrown an exception.
     * ER:
     * 1. Exception.
     * 2. Output should contain 1 record with OUTPUT_VALUE tag.
     * 3. Exception.
     *
     * @throws LinkException Should raise.
     */
    @Test
    final void testExtractor() throws LinkException {
        final Context context = this.factory.createObject(Context.class);
        final Output output = this.factory.createObject(Output.class);
        MatcherAssert.assertThat(
            "Should raise an exception because didn't init a schema",
            () -> this.extractor.process(context, output),
            ExceptionMatchers.catchException(
                LinkException.class,
                ExceptionMatchers.messageMatcher(
                    0, Matchers.containsString("You didn't init a schema!")
                )
            )
        );
        this.extractor.setSchema(ExtractorTest.SCHEMA);
        this.extractor.process(context, output);
        MatcherAssert.assertThat(
            "Output has incorrect values",
            output,
            Matchers.both(
                Matchers.<Record>iterableWithSize(1)
            ).and(
                Matchers.hasItem(KeeperMatchers.contain(ExtractorTestTags.OUTPUT_VALUE))
            )
        );
        context.put(ExtractorTestTags.EXCEPTION, new Object());
        MatcherAssert.assertThat(
            "Should raise an exception because didn't init a schema",
            () -> this.extractor.process(context, output),
            ExceptionMatchers.catchException(
                LinkException.class,
                ExceptionMatchers.messageMatcher(
                    1, Matchers.containsString("Can't extract for schema test.schema")
                )
            )
        );
    }

    /**
     * TC: Execute executor for different cases.
     * 1. Forgotten setup of schema.
     * 2. Executor create record in output.
     * 3. Executor thrown an exception.
     * ER:
     * 1. Exception.
     * 2. Output should contain 1 record with COUNT tag.
     * 3. Exception.
     *
     * @throws LinkException Should raise.
     */
    @Test
    final void testExecutor() throws LinkException {
        final Context context = this.factory.createObject(Context.class);
        final Output output = this.factory.createObject(Output.class);
        boolean exception = false;
        try {
            this.executor.process(context, output);
        } catch (final LinkException exp) {
            exception = exp.getMessage().contains("You didn't init a schema!");
        }
        assert exception;
        this.executor.setSchema(ExtractorTest.SCHEMA);
        this.executor.process(context, output);
        assert output.getRecords().size() == 1;
        assert output.getRecords().iterator().next().contains(StorageTags.COUNT);
        context.put(ExtractorTestTags.EXCEPTION, new Object());
        exception = false;
        try {
            this.executor.process(context, output);
        } catch (final LinkException exp) {
            exception = exp.getCause().getMessage()
                .contains("Can't extract for schema test.schema");
        }
        assert exception;
    }
}
