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

package insideworld.engine.actions.executors.profile;

import insideworld.engine.actions.Action;
import insideworld.engine.actions.executor.ActionExecutor;
import insideworld.engine.actions.executor.profiles.SystemExecuteProfile;
import insideworld.engine.actions.executors.TestExecutorTags;
import insideworld.engine.actions.keeper.context.Context;
import io.quarkus.test.junit.QuarkusTest;
import java.util.UUID;
import javax.inject.Inject;
import org.junit.jupiter.api.Test;

/**
 * Test execution of profiles.
 * @since 0.14.0
 */
@QuarkusTest
class TestProfile {

    /**
     * Class action executor.
     */
    private final ActionExecutor<Class<? extends Action>> executor;

    /**
     * Default constructor.
     * @param executor Class action executor.
     */
    @Inject
    TestProfile(final ActionExecutor<Class<? extends Action>> executor) {
        this.executor = executor;
    }

    /**
     * TC:
     * Add to default profile
     * Add to system profile pre executor with copy UUID tag in context.
     * ER:
     * Default executor profile shouldn't contain COPY_UUID tag.
     * System executor profile should contain COPY_UUID tag.
     * All profiles shouldn't execute DummyWrapper.
     */
    @Test
    final void testPreExecutor() {
        final UUID uuid = UUID.randomUUID();
        final Context def = this.executor.createContext();
        def.put(TestExecutorTags.UUID, uuid);
        this.executor.execute(DummyAction.class, def);
        assert !def.contains(TestExecutorTags.COPY_UUID);
        assert !def.contains(TestExecutorTags.DUMMY);
        final Context sys = this.executor.createContext();
        sys.put(TestExecutorTags.UUID, uuid);
        this.executor.execute(DummyAction.class, sys, SystemExecuteProfile.class);
        assert sys.contains(TestExecutorTags.COPY_UUID);
        assert !sys.contains(TestExecutorTags.DUMMY);
    }

}
