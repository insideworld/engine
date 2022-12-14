/*
 * Copyright (c) 2022 Anton Eliseev
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package insideworld.engine.entities.quarkus.beans;

import insideworld.engine.entities.converter.dto.mapper.Mapper;
import insideworld.engine.entities.storages.Storage;
import insideworld.engine.quarkus.AbstractBeans;
import java.util.Collection;
import java.util.stream.Collectors;
import javax.enterprise.inject.Instance;
import javax.enterprise.inject.Produces;

public class StorageBeans extends AbstractBeans {

    /**
     * Stupid CDI can't work properly with wildcards
     * @param instance
     * @return
     */
    @Produces
    public Collection<Storage> storages(final Instance<Storage<?>> instance) {
        return instance.stream().map(bean -> (Storage) bean)
            .collect(Collectors.toUnmodifiableList());
    }

    @Produces
    public Collection<Mapper> mappers(final Instance<Mapper> instance) {
        return this.fromInstance(instance);
    }
}
