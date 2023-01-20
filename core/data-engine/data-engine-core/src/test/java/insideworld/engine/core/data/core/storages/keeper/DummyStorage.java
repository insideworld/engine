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

package insideworld.engine.core.data.core.storages.keeper;

import insideworld.engine.core.data.core.StorageException;
import insideworld.engine.core.data.core.storages.Storage;
import java.util.Collection;
import java.util.Collections;

/**
 * Dummy storage implementation which not register in DI.
 * @since 0.14.0
 */
public class DummyStorage implements Storage<DummyEntity> {
    @Override
    public final Collection<DummyEntity> readAll() throws StorageException {
        return Collections.emptyList();
    }

    @Override
    public final DummyEntity read(final long id) throws StorageException {
        return null;
    }

    @Override
    public final Collection<DummyEntity> read(final Collection<Long> ids) throws StorageException {
        return Collections.emptyList();
    }

    @Override
    public final DummyEntity write(final DummyEntity entity) {
        return null;
    }

    @Override
    public final Collection<DummyEntity> writeAll(final Collection<DummyEntity> entity) {
        return Collections.emptyList();
    }

    @Override
    public final void delete(final Collection<DummyEntity> entities) {
        //Nothing to do.
    }

    @Override
    public final boolean exists(final long id) {
        return false;
    }

    @Override
    public final Class<? extends DummyEntity> forEntity() {
        return DummyEntity.class;
    }

}
