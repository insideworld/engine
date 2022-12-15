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

package insideworld.engine.core.data.core.converter;

import insideworld.engine.core.action.chain.Link;
import insideworld.engine.core.action.chain.LinkException;
import insideworld.engine.core.action.keeper.context.Context;
import insideworld.engine.core.action.keeper.output.Output;
import insideworld.engine.core.data.core.Entity;
import insideworld.engine.core.data.core.StorageException;
import insideworld.engine.core.data.core.tags.EntityTag;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;

/**
 * Import context to entity representation.
 * If entity already present in context with setup tag - this link will skip.
 *
 * @since 0.6.0
 */
@Dependent
public class ImportEntityLink implements Link {

    /**
     * Converter.
     */
    private final EntityConverter converter;

    /**
     * Entity tag.
     */
    private EntityTag<? extends Entity> tag;

    /**
     * Type of entity.
     */
    private Class<? extends Entity> type;

    /**
     * Default constructor.
     *
     * @param converter Entity converter.
     */
    @Inject
    public ImportEntityLink(final EntityConverter converter) {
        this.converter = converter;
    }

    @Override
    public final void process(final Context context, final Output output)
        throws LinkException, StorageException {
        if (this.tag == null || this.type == null) {
            throw new LinkException(
                this, "Link is not init: tag %s type %s", this.tag, this.type
            );
        }
        final Entity entity = this.converter.convert(context, this.type);
        context.put(this.tag.getTag(), entity);
    }

    @Override
    public final boolean can(final Context context) {
        return !context.contains(this.tag);
    }

    /**
     * Set tag and type to import.
     *
     * @param ptag Entity tag.
     * @param ptype Entity type.
     * @param <T> Entity type.
     */
    public final <T extends Entity> void setTag(final EntityTag<T> ptag, final Class<T> ptype) {
        this.tag = ptag;
        this.type = ptype;
    }
}
