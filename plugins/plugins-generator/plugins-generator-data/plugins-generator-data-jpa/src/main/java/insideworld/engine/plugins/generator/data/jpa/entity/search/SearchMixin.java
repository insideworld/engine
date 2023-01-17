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

package insideworld.engine.plugins.generator.data.jpa.entity.search;

import insideworld.engine.plugins.generator.data.jpa.entity.annotations.GenerateJpaEntity;
import insideworld.engine.plugins.generator.base.AbstractSearchMixin;
import insideworld.engine.plugins.generator.base.GenerateMixin;
import insideworld.engine.plugins.generator.base.reflection.Reflection;

public class SearchMixin
    extends AbstractSearchMixin<JpaInfo, GenerateJpaEntity>
    implements SearchEntities {


    public SearchMixin(final Reflection reflections) {
        super(reflections);
    }

    @Override
    protected Class<GenerateJpaEntity> annotation() {
        return GenerateJpaEntity.class;
    }

    @Override
    protected JpaInfo createSearch(
        final GenerateJpaEntity annotation, final Class<? extends GenerateMixin> mixin) {
        return new JpaInfo(
            annotation.entity(),
            annotation.schema(),
            annotation.table(),
            this.name(annotation.entity(), mixin),
            annotation.oneToOne(),
            true
        );
    }

    private String name(final Class<?> entity, final Class<? extends GenerateMixin> mixin) {
        return String.format(mixin.getPackageName() + ".generated.jpa.%s", entity.getSimpleName());
    }
}
