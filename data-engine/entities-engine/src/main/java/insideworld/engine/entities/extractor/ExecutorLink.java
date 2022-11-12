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

package insideworld.engine.entities.extractor;

import insideworld.engine.actions.chain.Link;
import insideworld.engine.actions.chain.LinkException;
import insideworld.engine.actions.keeper.context.Context;
import insideworld.engine.actions.keeper.output.Output;
import insideworld.engine.entities.StorageException;
import insideworld.engine.entities.tags.StorageTags;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;

/**
 * Link to call executor from chain action.
 * Need to set schema at init.
 *
 * @since 0.0.1
 */
@Dependent
public class ExecutorLink implements Link {

    /**
     * Extractor.
     */
    private final Extractor extractor;

    /**
     * Schema.
     */
    private String schema;

    /**
     * Default constructor.
     *
     * @param extractor Extractor instance.
     */
    @Inject
    public ExecutorLink(final Extractor extractor) {
        this.extractor = extractor;
    }

    @Override
    public final void process(final Context context, final Output output)
        throws LinkException, StorageException {
        if (this.schema == null) {
            throw new LinkException(this.getClass(), "You didn't init a schema!");
        }
        output.createRecord(this.schema).put(
            StorageTags.COUNT,
            this.extractor.execute(context, this.schema)
        );
    }

    /**
     * Set schema for call.
     *
     * @param pschema Schema.
     */
    public void setSchema(final String pschema) {
        this.schema = pschema;
    }
}
