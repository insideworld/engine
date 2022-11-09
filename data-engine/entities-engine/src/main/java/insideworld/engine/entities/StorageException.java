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

package insideworld.engine.entities;

import insideworld.engine.exception.CommonException;

/**
 * Storage exception.
 * @since 0.0.1
 */
public class StorageException extends CommonException {

    /**
     * Create exception based on message and entity type.
     * @param message Error message.
     * @param type Entity type.
     */
    public StorageException(
        final String message, final Class<? extends Entity> type) {
        super("%s for entity %s", message, type.getName());
    }

    /**
     * Create exception based on message, entity type and id.
     * @param message Error message.
     * @param type Entity type.
     * @param id ID of entity.
     */
    public StorageException(
        final String message, final Class<? extends Entity> type, final Long id) {
        super("%s for entity %s with id %d", message, type.getName(), id);
    }

    @Override
    protected final String module() {
        return "entities-engine";
    }
}