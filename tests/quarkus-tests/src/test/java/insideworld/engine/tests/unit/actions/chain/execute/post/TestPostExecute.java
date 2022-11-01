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

package insideworld.engine.tests.unit.actions.chain.execute.post;

import insideworld.engine.actions.Action;
import insideworld.engine.actions.executor.ActionExecutor;
import insideworld.engine.actions.keeper.context.Context;
import insideworld.engine.actions.keeper.output.Output;
import insideworld.engine.tests.unit.actions.chain.execute.TestChainTags;
import io.quarkus.test.junit.QuarkusTest;
import java.util.UUID;
import javax.inject.Inject;
import org.junit.jupiter.api.Test;

/**
 * Test for post execute.
 * See test method comments.
 * @since 0.14.0
 */
@QuarkusTest
public class TestPostExecute {

    /**
     * Class action executor.
     */
    private final ActionExecutor<Class<? extends Action>> executor;

    /**
     * Default constructor.
     * @param executor Class action executor.
     */
    @Inject
    public TestPostExecute(final ActionExecutor<Class<? extends Action>> executor) {
        this.executor = executor;
    }

    /**
     * TC:
     * Execute action with specific post-condition.
     * In child action add to context the additional tag and create 4 record where one record
     * has a copy of UUID from context.
     * In post-execute propagate to parent context the additional tag and one record from child
     * output which contain UUID tag.
     * ER: Output should contain only one record with UUID
     * Context should contain the additional tag with the ChildAction value.
     */
    @Test
    public void testPostExecute() {
        this.postExecute(ParentAction.class);
    }

    /**
     * The same as testPostExecute but using DI
     */
    @Test
    public void testPostExecuteDI() {
        this.postExecute(ParentDiAction.class);
    }

    /**
     * Common method for both tests.
     * @param action Action to execute.
     */
    private void postExecute(final Class<? extends Action> action) {
        final UUID uuid = UUID.randomUUID();
        final Context context = this.executor.createContext();
        context.put(TestChainTags.UUID, uuid);
        final Output output = this.executor.execute(action, context);
        assert "ChildAction".equals(context.get(TestChainTags.OUTPUT_ADDITIONAL));
        assert output.getRecords().size() == 1;
        assert uuid.equals(output.getRecords().iterator().next().get(TestChainTags.UUID));
    }

}
