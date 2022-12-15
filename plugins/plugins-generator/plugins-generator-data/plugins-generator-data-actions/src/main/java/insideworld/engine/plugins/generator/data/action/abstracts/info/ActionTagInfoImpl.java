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

package insideworld.engine.plugins.generator.data.action.abstracts.info;

import insideworld.engine.core.data.core.Entity;

public class ActionTagInfoImpl implements ActionTagInfo {

    private final Class<? extends Entity> entity;
    private final String key;
    private final String tag;
    private final Class<?>[] interfaces;
    private final String implementation;

    public ActionTagInfoImpl(final Class<? extends Entity> entity,
                             final String key,
                             final String tag,
                             final Class<?>[] interfaces,
                             final String implementation) {

        this.entity = entity;
        this.key = key;
        this.tag = tag;
        this.interfaces = interfaces;
        this.implementation = implementation;
    }

    @Override
    public Class<? extends Entity> entity() {
        return this.entity;
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
    public String tag() {
        return this.tag;
    }

    @Override
    public String implementation() {
        return this.implementation;
    }

}
