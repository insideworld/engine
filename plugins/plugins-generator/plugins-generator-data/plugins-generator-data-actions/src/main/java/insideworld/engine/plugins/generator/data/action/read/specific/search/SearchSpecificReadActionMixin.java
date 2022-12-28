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

package insideworld.engine.plugins.generator.data.action.read.specific.search;

import insideworld.engine.core.data.core.storages.Storage;
import insideworld.engine.plugins.generator.base.AbstractSearchMixin;
import insideworld.engine.plugins.generator.base.GenerateMixin;
import insideworld.engine.plugins.generator.base.reflection.Reflection;
import insideworld.engine.plugins.generator.data.action.read.specific.annotations.GenerateSpecificReadAction;
import insideworld.engine.plugins.generator.data.action.read.specific.info.SpecificReadInfo;
import insideworld.engine.plugins.generator.data.action.read.specific.info.SpecificReadInfoImpl;
import org.apache.commons.lang3.StringUtils;

public class SearchSpecificReadActionMixin
    extends AbstractSearchMixin<SpecificReadInfo, GenerateSpecificReadAction>
    implements SearchSpecificReadAction {

    public SearchSpecificReadActionMixin(final Reflection reflections) {
        super(reflections);
    }

    @Override
    protected SpecificReadInfo createSearch(GenerateSpecificReadAction annotation, Class<? extends GenerateMixin> mixin) {
        return new SpecificReadInfoImpl(
            annotation.storage(),
            annotation.key(),
            annotation.interfaces(),
            this.name(annotation.storage(), annotation.method(), mixin),
            annotation.method(),
            annotation.parameters()
        );
    }

    private String name(
        final Class<? extends Storage<?>> storage,
        final String method,
        final Class<? extends GenerateMixin> mixin
    ) {
        return String.format(
            mixin.getPackageName() + ".generated.actions.entity.Read%s%sAction",
            storage.getSimpleName(),
            StringUtils.capitalize(method)
        );
    }

    @Override
    protected Class<GenerateSpecificReadAction> annotation() {
        return GenerateSpecificReadAction.class;
    }
}
