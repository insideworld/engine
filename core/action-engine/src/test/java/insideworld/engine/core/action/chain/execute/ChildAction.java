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

package insideworld.engine.core.action.chain.execute;

import insideworld.engine.core.action.Action;
import insideworld.engine.core.common.exception.CommonException;
import java.util.UUID;
import javax.inject.Singleton;

/**
 * Child action which should execute by class executor.
 * Just propagate UUID from context to output and add OUTPUT_ADDITIONAL tag.
 * @since 0.14.0
 */
@Singleton
class ChildAction implements Action<UUID, ChildAction.Output> {

    @Override
    public Output execute(final UUID input) throws CommonException {
        return new Output() {
            @Override
            public UUID getUUID() {
                return input;
            }

            @Override
            public String getAdditional() {
                return "ChildAction";
            }
        };
    }

    @Override
    public final String key() {
        return "insideworld.engine.tests.unit.actions.chain.execute.simple.ChildAction";
    }

    @Override
    public final void types(final UUID input, final Output output) {
        //Nothing to do
    }

    public interface Output {

        UUID getUUID();

        String getAdditional();

    }
}
