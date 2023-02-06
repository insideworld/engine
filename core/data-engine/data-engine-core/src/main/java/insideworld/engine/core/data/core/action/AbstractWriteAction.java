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

package insideworld.engine.core.data.core.action;

import insideworld.engine.core.action.Action;
import insideworld.engine.core.common.exception.CommonException;
import insideworld.engine.core.data.core.Entity;
import insideworld.engine.core.data.core.storages.Storage;
import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Abstract action to write entity.
 * This class is necessary to fast creation of typical actions to write entity.
 * Entity should come to this action in raw representation.
 * First they import to context, then write and after that - exported.
 * Context arguments:
 * Raw entity.
 * Output results:
 * Raw entity.
 * @param <T> Type of entity.
 * @since 0.0.1
 */
public abstract class AbstractWriteAction<T extends Entity>
    implements Action<Collection<T>, Collection<T>> {

    private final String key;
    private final Storage<T> storage;

    @SuppressWarnings("unchecked")
    public AbstractWriteAction(final String key, final Storage<T> storage) {
        this.key = key;
        this.storage = storage;
    }

    @Override
    public Collection<T> execute(final Collection<T> input) throws CommonException {
        return this.storage.writeAll(input);
    }

    @Override
    public String key() {
        return this.key;
    }
}
