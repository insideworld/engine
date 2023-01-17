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

package insideworld.engine.frameworks.quarkus.common.threads;

import com.google.common.collect.Lists;
import insideworld.engine.core.common.injection.ObjectFactory;
import insideworld.engine.core.common.threads.MultiTask;
import insideworld.engine.core.common.threads.MultiTaskBuilder;
import insideworld.engine.core.common.threads.Task;
import insideworld.engine.core.common.threads.TaskBuilder;
import insideworld.engine.core.common.threads.TaskPredicate;
import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.infrastructure.Infrastructure;
import io.smallrye.mutiny.unchecked.Unchecked;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import javax.enterprise.context.Dependent;
import javax.enterprise.util.TypeLiteral;

@Dependent
public class QuarkusTaskBuilder<T> implements TaskBuilder<T> {

    private final ObjectFactory factory;

    private TaskPredicate<T> supplier;
    private Function<? super Throwable, ? extends T> exception;

    public QuarkusTaskBuilder(final ObjectFactory factory) {
        this.factory = factory;
    }

    @Override
    public TaskBuilder<T> add(final TaskPredicate<T> supplier) {
        this.supplier = supplier;
        return this;
    }

    public TaskBuilder<T> exception(final Function<? super Throwable, ? extends T> exception) {
        this.exception = exception;
        return this;
    }

    @Override
    public Task<T> build() {
        if (this.exception == null) {
            this.exception = (exception) -> null;
        }
        final QuarkusTask<T> task = this.factory.createObject(new TypeLiteral<>() {
        });
        final Uni<T> uni = Uni.createFrom()
            .item(Unchecked.supplier(supplier::execute))
            .runSubscriptionOn(Infrastructure.getDefaultExecutor())
            .onFailure()
            .recoverWithItem(exp -> {
                task.setThrowable(exp.getCause());
                return this.exception.apply(exp.getCause());
            });
        task.setUni(uni);
        return task;
    }


}
