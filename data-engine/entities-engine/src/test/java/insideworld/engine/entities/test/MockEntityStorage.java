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

package insideworld.engine.entities.test;

import insideworld.engine.entities.storages.Storage;
import insideworld.engine.entities.storages.StorageException;
import java.util.Collection;
import java.util.Collections;
import javax.inject.Singleton;

/**
 * Mock entity storage impl.
 * @since 0.14.0
 */
@Singleton
public class MockEntityStorage implements Storage<MockEntity> {
    @Override
    public final Collection<MockEntity> readAll() throws StorageException {
        return Collections.emptyList();
    }

    @Override
    public final MockEntity read(final long id) throws StorageException {
        return null;
    }

    @Override
    public final Collection<MockEntity> read(final Collection<Long> ids) throws StorageException {
        return Collections.emptyList();
    }

    @Override
    public final MockEntity write(final MockEntity entity) {
        return null;
    }

    @Override
    public final Collection<MockEntity> writeAll(final Collection<MockEntity> entity) {
        return Collections.emptyList();
    }

    @Override
    public final void delete(final Collection<MockEntity> entities) {
        //Nothing to do.
    }

    @Override
    public final boolean exists(final long id) {
        return false;
    }

    @Override
    public final Class<? extends MockEntity> forEntity() {
        return MockEntity.class;
    }
}
