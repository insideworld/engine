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

package insideworld.engine.data.generator.inmemory.storage.search;

import insideworld.engine.data.generator.StorageInfo;
import insideworld.engine.data.generator.inmemory.storage.annotations.GenerateInMemoryCrud;
import insideworld.engine.generator.AbstractSearchMixin;
import insideworld.engine.generator.GenerateMixin;
import insideworld.engine.generator.reflection.Reflection;

public class SearchMixin
    extends AbstractSearchMixin<StorageInfo, GenerateInMemoryCrud>
    implements SearchStorages {

    public SearchMixin(final Reflection reflections) {
        super(reflections);
    }

    @Override
    protected StorageInfo createSearch(
        final GenerateInMemoryCrud annotation, final Class<? extends GenerateMixin> mixin) {
        return new StorageInfo(
            annotation.entity(),
            annotation.override(),
            this.name(annotation.entity(), mixin)
        );
    }

    @Override
    protected Class<GenerateInMemoryCrud> annotation() {
        return GenerateInMemoryCrud.class;
    }

    private String name(final Class<?> entity, final Class<? extends GenerateMixin> mixin) {
        return String.format(
            mixin.getPackageName() + ".generated.storage.inmemory.%sStorage",
            entity.getSimpleName()
        );
    }
}
