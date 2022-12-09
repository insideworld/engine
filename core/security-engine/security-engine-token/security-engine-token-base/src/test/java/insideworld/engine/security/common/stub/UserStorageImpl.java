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

package insideworld.engine.security.common.stub;

import insideworld.engine.data.generator.inmemory.storage.AbstractInMemoryStorageDecorator;
import insideworld.engine.data.generator.inmemory.storage.abstracts.AbstractMemoryStorage;
import insideworld.engine.entities.StorageException;
import insideworld.engine.exception.CommonException;
import insideworld.engine.security.common.AuthenticationException;
import insideworld.engine.security.common.entities.User;
import insideworld.engine.security.common.storages.UserStorage;
import java.util.Collection;
import java.util.Optional;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class UserStorageImpl extends AbstractInMemoryStorageDecorator<User> implements UserStorage {

    @Inject
    public UserStorageImpl(final AbstractMemoryStorage<User> storage) {
        super(storage);
    }

    @Override
    public User getByToken(final String token) throws CommonException {
        return this.storage
            .readAll()
            .stream()
            .filter(user -> token.equals(user.getToken()))
            .findFirst()
            .orElseThrow(() -> new AuthenticationException("Token not found"));
    }

    @Override
    public Optional<User> getByName(final String name) throws StorageException {
        return this.storage
            .readAll()
            .stream()
            .filter(user -> name.equals(user.getName()))
            .findFirst();
    }

    @Override
    public Collection<User> getByNames(final Collection<String> names) throws StorageException {
        return this.storage.readAll()
            .stream().filter(user -> names.contains(user.getName())).toList();
    }
}
