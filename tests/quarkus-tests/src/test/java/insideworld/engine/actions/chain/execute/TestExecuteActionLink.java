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

package insideworld.engine.actions.chain.execute;

import insideworld.engine.actions.Action;
import insideworld.engine.actions.executor.ActionExecutor;
import insideworld.engine.actions.executor.impl.ClassActionExecutor;
import insideworld.engine.actions.keeper.context.Context;
import insideworld.engine.injection.ObjectFactory;
import io.quarkus.test.junit.QuarkusTest;
import javax.enterprise.util.TypeLiteral;
import javax.inject.Inject;
import org.junit.jupiter.api.Test;

@QuarkusTest
public class TestExecuteActionLink {

    private final ClassActionExecutor executor;

//    private ActionExecutor<Class<? extends Action>> qwe;
    private ObjectFactory factory;

    @Inject
    public TestExecuteActionLink(final ClassActionExecutor executor,
//                                 final ActionExecutor<Class<? extends Action>> qwe,
                                 final ObjectFactory factory) {
        this.executor = executor;
//        this.qwe = qwe;
        this.factory = factory;
    }

    @Test
    public void test() {
        final Context context = this.executor.createContext();
        final ActionExecutor<Class<? extends Action>> object = factory.createObject(new TypeLiteral<ActionExecutor<Class<? extends Action>>>() {});
        this.executor.execute(PrimaryAction.class, context);
        StaticTest.qwe();
    }

}
