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

package insideworld.engine.data.jpa;

import insideworld.engine.entities.Entity;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public abstract class AbstractCrudGenericStorage<T extends Entity, C extends T>
    extends AbstractCrudStorage<T, C> {

    private final Class<C> crud;
    private final Class<T> entity;

    public AbstractCrudGenericStorage() {
        final ParameterizedType generics =
            (ParameterizedType) this.getClass().getGenericSuperclass();
        final Type[] arguments = generics.getActualTypeArguments();
        this.entity = (Class<T>) arguments[0];
        this.crud = (Class<C>) arguments[1];
    }

    @Override
    protected final Class<C> forCrud() {
        return this.crud;
    }

    @Override
    public final Class<T> forEntity() {
        return this.entity;
    }

}
