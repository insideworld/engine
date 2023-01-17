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

package insideworld.engine.plugins.generator.data.action.read.specific.info;

import insideworld.engine.core.data.core.Entity;
import insideworld.engine.core.data.core.storages.Storage;

public class SpecificReadInfoImpl implements SpecificReadInfo {

    private final Class<? extends Storage<? extends Entity>> storage;
    private final String key;
    private final Class<?>[] interfaces;
    private final String implementation;

    private final Class<?> input;

    private final String method;

    public SpecificReadInfoImpl(
        Class<? extends Storage<?>> storage,
        final String key,
        final Class<?>[] interfaces,
        final String implementation,
        final String method,
        final Class<?> input
    ) {
        this.storage = storage;
        this.key = key;
        this.interfaces = interfaces;
        this.implementation = implementation;
        this.method = method;
        this.input = input;
    }

    @Override
    public Class<? extends Storage<? extends Entity>> storage() {
        return this.storage;
    }

    @Override
    public String key() {
        return this.key;
    }

    @Override
    public Class<?>[] interfaces() {
        return this.interfaces;
    }

    @Override
    public String implementation() {
        return this.implementation;
    }

    @Override
    public String method() {
        return this.method;
    }

    public Class<?> getInput() {
        return this.input;
    }
}
