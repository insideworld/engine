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
import org.apache.commons.collections4.CollectionUtils;
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
public abstract class AbstractReadAction<T extends Entity>
    implements Action<Collection<Long>, Collection<T>> {

    private final String key;
    private final Storage<T> storage;

    @SuppressWarnings("unchecked")
    public AbstractReadAction(final String key, final Storage<T> storage) {
        this.key = key;
        this.storage = storage;
    }


    @Override
    public Collection<T> execute(final Collection<Long> input) throws CommonException {
        final Collection<T> result;
        if (CollectionUtils.isEmpty(input)) {
            result = this.storage.readAll();
        } else {
            result = this.storage.read(input);
        }
        return result;
    }

    @Override
    public String key() {
        return this.key;
    }

}
