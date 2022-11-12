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
import insideworld.engine.actions.chain.Link;
import insideworld.engine.actions.chain.LinkException;
import insideworld.engine.actions.keeper.context.Context;
import insideworld.engine.actions.keeper.output.Output;
import insideworld.engine.entities.Entity;
import insideworld.engine.entities.StorageException;
import insideworld.engine.entities.tags.EntitiesTag;
import insideworld.engine.entities.tags.EntityTag;
import java.util.Collection;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;

/**
 * Export entity or entities to output.
 * @since 0.0.6
 */
@Dependent
public class ExportEntityLink implements Link {

    /**
     * Entity converter.
     */
    private final EntityConverter converter;

    /**
     * Single entity tag.
     */
    private EntityTag<?> single;

    /**
     * Multiple entities tag.
     */
    private EntitiesTag<?> multiple;

    /**
     * Default constructor.
     * @param converter Entity converter.
     */
    @Inject
    public ExportEntityLink(final EntityConverter converter) {
        this.converter = converter;
    }

    @Override
    public final void process(final Context context, final Output output) throws LinkException {
        if (this.single == null && this.multiple == null) {
            throw new LinkException(this.getClass(), "Link is not init!");
        }
        final Collection<Entity> entities = Lists.newLinkedList();
        context.getOptional(this.single).ifPresent(entities::add);
        context.getOptional(this.multiple).ifPresent(entities::addAll);
        for (final Entity entity : entities) {
            try {
                output.add(this.converter.convert(entity));
            } catch (final StorageException exp) {
                throw this.exception(exp);
            }
        }
    }

    /**
     * Set single tag to export.
     * Make single record in output.
     * @param tag Entity tag.
     * @return The same instance.
     */
    public final ExportEntityLink setTag(final EntityTag<?> tag) {
        this.single = tag;
        return this;
    }

    /**
     * Set multiple tag to export.
     * Make as many records in output than exists in collection under the tag.
     * @param tag Multiple tag.
     * @return The same instance.
     */
    public final ExportEntityLink setTag(final EntitiesTag<?> tag) {
        this.multiple = tag;
        return this;
    }
}
