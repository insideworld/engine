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
import insideworld.engine.threads.Task;
import insideworld.engine.threads.TaskException;
import io.smallrye.mutiny.Uni;
import java.util.Collection;
import java.util.function.Consumer;

public class QuarkusTask<T> implements Task<T> {

    private Uni<T> uni;

    private final Collection<Throwable> throwables = Lists.newLinkedList();

    @Override
    public T result() {
        return this.uni.await().indefinitely();
    }

    @Override
    public void subscribe(final Consumer<T> callback) {
        this.uni.subscribe().with(callback);
    }

    @Override
    public final Collection<Throwable> exceptions() {
        return this.throwables;
    }

    public void addThrowable(Throwable throwable) {
        this.throwables.add(throwable);
    }

    public void setUni(final Uni<T> puni) {
        this.uni = puni;
    }
}
