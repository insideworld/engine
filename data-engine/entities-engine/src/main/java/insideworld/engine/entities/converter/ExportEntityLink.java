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

package insideworld.engine.entities.converter;

import com.google.common.collect.Lists;
import insideworld.engine.actions.ActionException;
import insideworld.engine.actions.chain.Link;
import insideworld.engine.actions.keeper.context.Context;
import insideworld.engine.actions.keeper.output.Output;
import insideworld.engine.entities.Entity;
import insideworld.engine.entities.tags.EntitiesTag;
import insideworld.engine.entities.tags.EntityTag;
import java.util.Collection;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;

/**
 * Export entity to DTO representation.
 */
@Dependent
public class ExportEntityLink implements Link {

    private final EntityConverter converter;

    private EntityTag<?> single;

    private EntitiesTag<?> multiple;

    @Inject
    public ExportEntityLink(final EntityConverter converter) {
        this.converter = converter;
    }

    @Override
    public void process(final Context context, final Output output) throws ActionException {
        final Collection<Entity> entities = Lists.newLinkedList();
        context.getOptional(this.single).ifPresent(entities::add);
        context.getOptional(this.multiple).ifPresent(entities::addAll);
        for (final Entity entity : entities) {
            output.add(this.converter.convert(entity));
        }
    }

    public ExportEntityLink setTag(final EntityTag<?> tag) {
        this.single = tag;
        return this;
    }

    public ExportEntityLink setTag(final EntitiesTag<?> tag) {
        this.multiple = tag;
        return this;
    }
}
