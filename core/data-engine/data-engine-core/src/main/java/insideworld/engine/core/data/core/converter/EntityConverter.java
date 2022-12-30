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

package insideworld.engine.core.data.core.converter;

import insideworld.engine.core.data.core.Entity;
import insideworld.engine.core.data.core.StorageException;
import insideworld.engine.core.common.keeper.Record;
/**
 * Interface to convert entity to record and vice versa.
 * @see Entity
 * @since 0.0.1
 */
public interface EntityConverter {

    /**
     * Map entity to record.
     * @param entity Entity to convert.
     * @return Record with raw values.
     * @throws StorageException Can't convert entity.
     */
    Record convert(Entity entity) throws StorageException;

    /**
     * Map record to entity.
     * @param record Record with raw entity.
     * @param type Type of entity.
     * @param <T> Type of entity.
     * @return Entity.
     * @throws StorageException Can't convert entity.
     */
    <T extends Entity> T convert(Record record, Class<T> type) throws StorageException;

}
