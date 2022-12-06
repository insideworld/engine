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

import insideworld.engine.actions.ActionsTestTags;
import insideworld.engine.actions.chain.TestChainTags;
import insideworld.engine.actions.keeper.Record;
import insideworld.engine.actions.keeper.context.Context;
import insideworld.engine.actions.keeper.output.Output;
import insideworld.engine.exception.CommonException;
import insideworld.engine.injection.ObjectFactory;
import io.quarkus.test.junit.QuarkusTest;
import javax.inject.Inject;
import org.junit.jupiter.api.Test;

/**
 * Test ContextToOutput link.
 * @since 0.14.0
 */
@QuarkusTest
class ContextToOutputTest {

    /**
     * Object factory.
     */
    private final ObjectFactory factory;

    /**
     * Default constructor.
     * @param factory Object factory.
     */
    @Inject
    ContextToOutputTest(final ObjectFactory factory) {
        this.factory = factory;
    }

    /**
     * TC: Create a context with different simple, mandatory and system tags.
     * Execute link with different condition and use different methods to set tags.
     * ER: Mandatory tags should be present in any case.
     * System tags should absent even if required.
     * @throws CommonException Shouldn't raise.
     */
    @Test
    final void test() throws CommonException {
        final Context context = this.factory.createObject(Context.class);
        context.put(TestChainTags.ONE, new Object());
        context.put(TestChainTags.TWO, new Object());
        context.put(TestChainTags.STRING.getTag(), new Object());
        context.put(TestChainTags.INTEGER.getTag(), new Object());
        context.put(ActionsTestTags.SYSTEM, new Object());
        context.put(ActionsTestTags.MANDATORY, new Object());
        this.checkAll(context);
        this.checkSingle(context);
        this.checkMultiTags(context);
        this.checkMultiString(context);
    }

    /**
     * Call link for move all.
     * @param context Context with tags.
     * @throws CommonException Shouldn't raise.
     */
    private void checkAll(final Context context) throws CommonException {
        final Output output = this.factory.createObject(Output.class);
        final ContextToOutput link = this.factory.createObject(ContextToOutput.class);
        link.process(context, output);
        assert output.getRecords().size() == 1;
        final Record record = output.iterator().next();
        assert record.values().size() == 5;
        assert record.contains(TestChainTags.ONE);
        assert record.contains(TestChainTags.TWO);
        assert record.contains(TestChainTags.STRING);
        assert record.contains(TestChainTags.INTEGER);
        assert record.contains(ActionsTestTags.MANDATORY);
        assert !record.contains(ActionsTestTags.SYSTEM);
    }

    /**
     * Call link with added single tags.
     * @param context Context with tags.
     * @throws CommonException Shouldn't raise.
     */
    private void checkSingle(final Context context) throws CommonException {
        final Output output = this.factory.createObject(Output.class);
        final ContextToOutput link = this.factory.createObject(ContextToOutput.class);
        link
            .addTag(TestChainTags.ONE)
            .addTag(TestChainTags.TWO.getTag())
            .addTag(TestChainTags.INTEGER)
            .addTag(ActionsTestTags.SYSTEM);
        link.process(context, output);
        assert output.getRecords().size() == 1;
        final Record record = output.iterator().next();
        assert record.values().size() == 4;
        assert record.contains(TestChainTags.ONE);
        assert record.contains(TestChainTags.TWO);
        assert !record.contains(TestChainTags.STRING);
        assert record.contains(TestChainTags.INTEGER);
        assert record.contains(ActionsTestTags.MANDATORY);
        assert !record.contains(ActionsTestTags.SYSTEM);
    }

    /**
     * Call link with added tags by array method.
     * @param context Context with tags.
     * @throws CommonException Shouldn't raise.
     */
    private void checkMultiTags(final Context context) throws CommonException {
        final Output output = this.factory.createObject(Output.class);
        final ContextToOutput link = this.factory.createObject(ContextToOutput.class);
        link
            .addTags(TestChainTags.ONE, TestChainTags.INTEGER)
            .addTag(TestChainTags.TWO.getTag());
        link.process(context, output);
        assert output.getRecords().size() == 1;
        final Record record = output.iterator().next();
        assert record.values().size() == 4;
        assert record.contains(TestChainTags.ONE);
        assert record.contains(TestChainTags.TWO);
        assert !record.contains(TestChainTags.STRING);
        assert record.contains(TestChainTags.INTEGER);
        assert record.contains(ActionsTestTags.MANDATORY);
        assert !record.contains(ActionsTestTags.SYSTEM);
    }

    /**
     * Call link with added string tags by arrays method.
     * @param context Context with tags.
     * @throws CommonException Shouldn't raise.
     */
    private void checkMultiString(final Context context) throws CommonException {
        final Output output = this.factory.createObject(Output.class);
        final ContextToOutput link = this.factory.createObject(ContextToOutput.class);
        link
            .addTags(TestChainTags.ONE.getTag(), TestChainTags.INTEGER.getTag())
            .addTag(TestChainTags.TWO);
        link.process(context, output);
        assert output.getRecords().size() == 1;
        final Record record = output.iterator().next();
        assert record.values().size() == 4;
        assert record.contains(TestChainTags.ONE);
        assert record.contains(TestChainTags.TWO);
        assert !record.contains(TestChainTags.STRING);
        assert record.contains(TestChainTags.INTEGER);
        assert record.contains(ActionsTestTags.MANDATORY);
        assert !record.contains(ActionsTestTags.SYSTEM);
    }

}
