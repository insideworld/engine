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

package insideworld.engine.core.action.chain.builder;

import insideworld.engine.core.action.executor.ActionExecutor;
import insideworld.engine.core.action.executor.key.ClassKey;
import insideworld.engine.core.common.exception.CommonException;
import io.quarkus.test.junit.QuarkusTest;
import javax.inject.Inject;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

/**
 * Test link builder.
 * @since 0.14.0
 */
@QuarkusTest
class TestBuilder {

    /**
     * Class action executor.
     */
    private final ActionExecutor executor;

    /**
     * Default constructor.
     * @param executor Class action executor.
     */
    @Inject
    TestBuilder(final ActionExecutor executor) {
        this.executor = executor;
    }

    /**
     * TC:
     * Create action with different links added by TypeLiteral and init.
     * Each link just add to context a specific tag.
     * ER:
     * All 4 tags should present in context.
     * @throws CommonException Exception.
     */
    @Test
    final void test() throws CommonException {
        final Input execute = this.executor.execute(
            new ClassKey<>(TestAction.class),
            new Input()
        );
        MatcherAssert.assertThat(
            "STRING is absent",
            execute.getString(),
            Matchers.is("Default")
        );
        MatcherAssert.assertThat(
            "INTEGER tag is absent",
            execute.getInteger(),
            Matchers.is(0)
        );
        MatcherAssert.assertThat(
            "UUID tag is absent",
            execute,
            Matchers.notNullValue()
        );
//        MatcherAssert.assertThat(
//            "UUID_ADDITIONAL tag is absent",
//            context,
//            KeeperMatchers.contain(TestChainTags.UUID_ADDITIONAL)
//        );
    }

}
