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

package insideworld.engine.datatransfer.endpoint.actions;


import insideworld.engine.actions.keeper.Record;
import insideworld.engine.actions.keeper.output.Output;
import insideworld.engine.exception.CommonException;
import insideworld.engine.injection.ObjectFactory;
import insideworld.engine.threads.Task;
import insideworld.engine.threads.TaskBuilder;
import insideworld.engine.threads.TaskPredicate;
import java.util.Collection;
import javax.enterprise.util.TypeLiteral;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class OutputTaskImpl implements OutputTask {

    private final ObjectFactory factory;

    @Inject
    public OutputTaskImpl(final ObjectFactory factory) {
        this.factory = factory;
    }

    @Override
    public Task<Output> createTask(final Collection<TaskPredicate<Output>> predicates) {
        final TaskBuilder<Output, Output> builder =
            this.factory.createObject(new TypeLiteral<>() { });
        for (final TaskPredicate<Output> predicate : predicates) {
            builder.add(predicate);
        }
        builder.combine(outputs -> {
            final Output output = this.factory.createObject(Output.class);
            outputs.forEach(output::merge);
            return output;
        }, Output.class);
        builder.exception(exp -> {
            final Output output = this.factory.createObject(Output.class);
            final CommonException common = (CommonException) exp.getCause();
            common.getIndexes().forEach(index -> {
                final Record record = output.createRecord();
                record.put("index", index.index());
                record.put("key", index.diagnostic().key());
                record.put("value", index.diagnostic().value());
            });
            return output;
        });
        return builder.build();
    }

}
