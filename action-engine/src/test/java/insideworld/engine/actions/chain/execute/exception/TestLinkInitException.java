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

package insideworld.engine.actions.chain.execute.exception;

import insideworld.engine.actions.Action;
import insideworld.engine.actions.ActionException;
import insideworld.engine.actions.chain.AbstractChainAction;
import insideworld.engine.actions.chain.LinkInitException;
import insideworld.engine.actions.chain.LinksBuilder;
import insideworld.engine.actions.startup.ActionsInit;
import insideworld.engine.exception.CommonException;
import io.quarkus.test.junit.QuarkusTest;
import java.util.Collections;
import java.util.List;
import javax.inject.Inject;
import org.junit.jupiter.api.Test;

/**
 * Test exception at link init.
 * @since 0.14.0
 */
@QuarkusTest
class TestLinkInitException {

    /**
     * Link builder.
     */
    private final LinksBuilder builder;

    /**
     * Action lists to test twice init.
     */
    private final List<Action> actions;

    /**
     * Default constructor.
     * @param builder Link builder.
     * @param actions All actions.
     */
    @Inject
    TestLinkInitException(final LinksBuilder builder, final List<Action> actions) {
        this.builder = builder;
        this.actions = actions;
    }

    /**
     * TC: Just execute an action which should fail an init.
     * ER: Exception with message.
     * @checkstyle BooleanExpressionComplexityCheck (20 lines)
     */
    @Test
    final void testLinkInit() {
        final ActionsInit init = new ActionsInit(
            Collections.singletonList(new TestAction(this.builder)),
            Collections.emptyList()
        );
        boolean exception = false;
        try {
            init.startUp();
        } catch (final CommonException exp) {
            exception =
                exp.getClass().equals(ActionException.class)
                && exp.getCause().getClass().equals(LinkInitException.class)
                && exp.getCause().getMessage().contains("LinkWithException")
                && exp.getCause().getCause().getClass().equals(IllegalArgumentException.class)
                && exp.getCause().getCause().getMessage().equals("Exception at link init");
        }
        assert exception;
    }

    /**
     * TC: Just execute twice all init on action.
     * We already have chain actions in DI.
     * ER: Should be exception about call init links twice.
     */
    @Test
    final void testTwiceInit() {
        boolean exception = false;
        for (final Action action : this.actions) {
            try {
                if (AbstractChainAction.class.isAssignableFrom(action.getClass())) {
                    action.init();
                }
            } catch (final ActionException exp) {
                if (exp.getMessage().contains("Chain action already init!")) {
                    exception = true;
                    break;
                }
            }
        }
        assert exception;
    }

}
