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

package insideworld.engine.core.data.core.extractor;

import insideworld.engine.core.action.keeper.Record;
import insideworld.engine.core.action.keeper.output.Output;
import insideworld.engine.core.data.core.StorageException;
import insideworld.engine.core.common.injection.ObjectFactory;
import java.util.Map;
import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Mock extractor.
 * @since 0.14.0
 */
@Singleton
public class MockExtractor implements Extractor {

    /**
     * Object factory.
     */
    private final ObjectFactory factory;

    /**
     * Default constructor.
     * @param factory Object factory.
     */
    @Inject
    public MockExtractor(final ObjectFactory factory) {
        this.factory = factory;
    }

    @Override
    public final Output extract(final Map<String, ?> context, final String schema) {
        return null;
    }

    @Override
    public final Output extract(final Record record, final String schema) throws StorageException {
        if (record.contains(ExtractorTestTags.EXCEPTION)) {
            throw new StorageException(
                new IllegalArgumentException("Extract"),
                "Can't extract for schema %s",
                schema
            );
        }
        final Output output = this.factory.createObject(Output.class);
        output.createRecord().put(ExtractorTestTags.OUTPUT_VALUE, new Object());
        return output;
    }

    @Override
    public final int execute(final Map<String, ?> context, final String schema) {
        return 0;
    }

    @Override
    public final int execute(final Record record, final String schema) throws StorageException {
        return this.extract(record, schema).getRecords().size();
    }
}
