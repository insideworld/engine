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

package insideworld.engine.example.quarkus.common.data;

import insideworld.engine.core.data.jpa.AbstractCrudStorage;
import insideworld.engine.plugins.generator.data.jpa.storage.AbstractCrudDecorator;
import java.util.Collection;
import java.util.Date;
import javax.inject.Singleton;

@Singleton
public class SomeDataStorageCrud extends AbstractCrudDecorator<SomeData> implements SomeDataStorage {

    public SomeDataStorageCrud(AbstractCrudStorage<SomeData, ? extends SomeData> storage) {
        super(storage);
    }

    @Override
    public Collection<SomeData> readByValue(final String value) {
        return (Collection<SomeData>) this.storage
            .find("from SomeData c where c.value = ?1", value)
            .list();
    }

    @Override
    public SomeData readByValueSingle(final String value) {
        return this.storage
            .find("from SomeData c where c.value = ?1", value).firstResult();
    }

    @Override
    public Collection<SomeData> readByDateAndValue(final String value, final Date date) {
        return (Collection<SomeData>) this.storage
            .find("from SomeData c where c.value = ?1 and c.date = ?2", value, date)
            .list();
    }
}
