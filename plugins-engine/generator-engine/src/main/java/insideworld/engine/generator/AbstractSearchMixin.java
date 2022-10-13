/*
 * Copyright (c) 2022 Anton Eliseev
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package insideworld.engine.generator;

import insideworld.engine.generator.reflection.Reflection;
import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;
import org.apache.commons.lang3.tuple.Pair;

public abstract class AbstractSearchMixin<T, U extends Annotation> {

    private final Reflection reflections;

    public AbstractSearchMixin(final Reflection reflections) {
        this.reflections = reflections;
    }

    public Collection<T> search() {
        return this.reflections.getSubTypesOf(GenerateMixin.class).stream()
            .map(mixin -> Pair.of(mixin, mixin.getAnnotationsByType(this.annotation())))
            .flatMap(pair -> Arrays.stream(pair.getRight()).map(
                annotation -> this.createSearch(annotation, pair.getLeft())
            ))
            .collect(Collectors.toList());
    }

    protected abstract T createSearch(U annotation, Class<? extends GenerateMixin> mixin);

    protected abstract Class<U> annotation();

}
