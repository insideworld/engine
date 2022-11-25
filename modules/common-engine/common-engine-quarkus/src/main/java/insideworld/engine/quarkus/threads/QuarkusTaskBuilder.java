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

package insideworld.engine.quarkus.threads;

import com.google.common.collect.Lists;
import com.google.common.collect.Queues;
import insideworld.engine.injection.ObjectFactory;
import insideworld.engine.threads.Task;
import insideworld.engine.threads.TaskBuilder;
import insideworld.engine.threads.TaskPredicate;
import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.infrastructure.Infrastructure;
import io.smallrye.mutiny.unchecked.Unchecked;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;
import javax.enterprise.context.Dependent;
import javax.enterprise.util.TypeLiteral;

@Dependent
public class QuarkusTaskBuilder<T, O> implements TaskBuilder<T, O> {

    private final Collection<TaskPredicate<T>> suppliers;
    private final ObjectFactory factory;

    private Function<List<T>, O> function;
    private Class<T> type;

    private int concurrency = 2;
    private Function<? super Throwable, ? extends T> exception;

    public QuarkusTaskBuilder(final ObjectFactory factory) {
        this.factory = factory;
        this.suppliers = Lists.newLinkedList();
    }

    @Override
    public TaskBuilder<T, O> add(final TaskPredicate<T> supplier) {
        this.suppliers.add(supplier);
        return this;
    }

    @Override
    public TaskBuilder<T, O> combine(final Function<List<T>, O> function, Class<T> type) {
        this.function = function;
        this.type = type;
        return this;
    }

    public TaskBuilder<T, O> exception(final Function<? super Throwable, ? extends T> exception) {
        this.exception = exception;
        return this;
    }

    @Override
    public TaskBuilder<T, O> concurrencyLevel(int level) {
        this.concurrency = level;
        return this;
    }

    @Override
    public Task<O> build() {
        final QuarkusTask<O> task = this.factory.createObject(new TypeLiteral<>() { });
        final Uni<O> uni = Uni.combine().all().unis(
                this.suppliers.stream().map(
                        supplier -> Uni
                            .createFrom()
                            .item(Unchecked.supplier(supplier::execute))
                            .runSubscriptionOn(Infrastructure.getDefaultWorkerPool())
                            .onFailure()
                            .recoverWithItem(exp -> {
                                task.addThrowable(exp.getCause());
                                return this.exception.apply(exp.getCause());
                            })
                    )
                    .toList()
            )
            .usingConcurrencyOf(this.concurrency)
            .combinedWith(this.type, this.function);
        task.setUni(uni);
        return task;
    }

}
