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
import java.util.List;
import org.apache.commons.lang3.ArrayUtils;

/**
 * Abstract action to read entity by ID or IDS tags.
 * This class is necessary to fast creation of typical actions to read entity.
 * Input arguments:
 * insideworld.engine.core.data.core.tags.StorageTags#ID - for read only single entity.
 * or
 * insideworld.engine.core.data.core.tags.StorageTags#IDS - for read several entities.
 * All read entity or entities will add to output using ExportEntityLink.
 *
 * @param <T> Type of entity.
 * @since 0.0.1
 */
public class ReadAction<T extends Entity> implements Action<Long[], T[]> {

    private final T[] array;

    private final Class<T[]> type;
    private final String key;
    private final Storage<T> storage;

    @SuppressWarnings("unchecked")
    public ReadAction(final String key, final Storage<T> storage) {
        this.key = key;
        this.storage = storage;
        this.array = (T[]) Array.newInstance(this.storage.forEntity(), 0);
        this.type = (Class<T[]>) this.array.getClass();
    }


    @Override
    public T[] execute(final Long[] input) throws CommonException {
        final T[] result;
        if (ArrayUtils.isEmpty(input)) {
            result = this.storage.readAll().toArray(this.array);
        } else {
            result = this.storage.read(List.of(input)).toArray(this.array);
        }
        return result;
    }

    @Override
    public String key() {
        return this.key;
    }

    @Override
    public Class<? extends Long[]> inputType() {
        return Long[].class;
    }

    @Override
    public Class<? extends T[]> outputType() {
        return this.type;
    }
}
