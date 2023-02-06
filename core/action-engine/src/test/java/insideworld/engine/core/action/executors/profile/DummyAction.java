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

package insideworld.engine.core.action.executors.profile;

import insideworld.engine.core.action.Action;
import insideworld.engine.core.common.exception.CommonException;
import javax.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * If it has sequence to test wrapper orders - add zero and all.
 *
 * @since 0.14.0
 */
@Singleton
class DummyAction implements Action<Void, Void> {

    /**
     * Logger.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(DummyAction.class);
    private final TestObject test;

    public DummyAction(final TestObject test) {
        this.test = test;
    }

    @Override
    public final Void execute(final Void input) throws CommonException {
        DummyAction.LOGGER.debug("Wrapper 0");
        this.test.integers.add(0);
        return null;
    }

    @Override
    public final String key() {
        return "insideworld.engine.tests.unit.actions.executors.profile.DummyAction";
    }

    @Override
    public final void types(final Void input, final Void output) {
        //Nothing to do
    }
}
