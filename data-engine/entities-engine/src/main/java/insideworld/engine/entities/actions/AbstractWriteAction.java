/*
 * Copyright (c) 2022 Anton Eliseev
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package insideworld.engine.entities.actions;

import insideworld.engine.actions.chain.AbstractChainAction;
import insideworld.engine.actions.chain.Link;
import insideworld.engine.actions.chain.LinksBuilder;
import insideworld.engine.entities.Entity;
import insideworld.engine.entities.actions.links.WriteEntityLink;
import insideworld.engine.entities.converter.ExportEntityLink;
import insideworld.engine.entities.converter.ImportEntityLink;
import insideworld.engine.entities.tags.EntityTag;
import java.util.Collection;
import javax.enterprise.util.TypeLiteral;

public abstract class AbstractWriteAction<T extends Entity> extends AbstractChainAction {

    public AbstractWriteAction(LinksBuilder builder) {
        super(builder);
    }

    @Override
    protected Collection<Link> attachLinks(final LinksBuilder builder) {
        builder.addLink(ImportEntityLink.class, link -> link.setTag(this.getTag(), this.getType()));
        this.afterImport(builder);
        builder.addLink(
            new TypeLiteral<WriteEntityLink<T>>() {},
            link -> link.setTag(this.getTag()));
        builder.addLink(ExportEntityLink.class, link -> link.setTag(this.getTag()));
        return this.afterExport(builder).build();
    }

    protected abstract EntityTag<T> getTag();

    protected abstract Class<T> getType();

    protected LinksBuilder afterImport(final LinksBuilder builder) {
        return builder;
    }

    protected LinksBuilder afterExport(final LinksBuilder builder) { return builder; }

}
