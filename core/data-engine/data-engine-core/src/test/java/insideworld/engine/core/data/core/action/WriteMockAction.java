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

package insideworld.engine.core.data.core.action;

import insideworld.engine.core.action.chain.LinksBuilder;
import insideworld.engine.core.data.core.mock.MockTags;
import insideworld.engine.core.data.core.mock.entities.positive.MockEntity;
import insideworld.engine.core.data.core.tags.EntityTag;
import javax.inject.Singleton;

/**
 * Action to test write action.
 * @since 0.14.0
 */
@Singleton
class WriteMockAction extends AbstractWriteAction<MockEntity> {

    /**
     * Default constructor.
     *
     * @param builder Link builder.
     */
    WriteMockAction(final LinksBuilder builder) {
        super(builder);
    }

    @Override
    public final String key() {
        return "insideworld.engine.core.data.core.actions.WriteMockAction";
    }

    @Override
    protected final EntityTag<MockEntity> getTag() {
        return MockTags.PRIMARY;
    }

    @Override
    protected final Class<MockEntity> getType() {
        return MockEntity.class;
    }

    @Override
    protected final void afterImport(final LinksBuilder builder) {
        //Nothing to do.
    }

    @Override
    protected final void afterExport(final LinksBuilder builder) {
        //Nothing to do.
    }
}
