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
import insideworld.engine.core.common.predicates.Consumer;
import insideworld.engine.core.common.threads.MultiTask;
import insideworld.engine.core.common.threads.Task;
import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.unchecked.Unchecked;
import java.util.Collection;
import java.util.Collections;
import javax.enterprise.context.Dependent;

@Dependent
public class QuarkusTask<T> implements Task<T> {

    private Uni<T> uni;

    private Throwable throwable;


    @Override
    public Throwable exception() {
        return this.throwable;
    }

    @Override
    public T result() {
        return this.uni.await().indefinitely();
    }

    @Override
    public void subscribe(final Consumer<T> callback) {
        this.uni.subscribe().with(Unchecked.consumer(callback::accept));
    }

    public void setThrowable(final Throwable throwable) {
        this.throwable = throwable;
    }

    public void setUni(final Uni<T> puni) {
        this.uni = puni;
    }
}
