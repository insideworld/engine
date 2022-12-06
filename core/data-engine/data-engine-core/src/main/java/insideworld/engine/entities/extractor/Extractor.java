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

import insideworld.engine.actions.keeper.Record;
import insideworld.engine.actions.keeper.output.Output;
import insideworld.engine.entities.StorageException;
import java.util.Map;

/**
 * Extractor is using to get some information from database based on schema.
 * @since 0.0.1
 */
public interface Extractor {

    /**
     * Extract information by schema.
     * @param context Map with input arguments for schema.
     * @param schema Schema which using to extract data.
     * @return Output with data.
     * @throws StorageException Can't extract.
     */
    Output extract(Map<String, ?> context, String schema) throws StorageException;

    /**
     * Extract information by schema.
     * @param record Record with input parameters.
     * @param schema Schema which using to extract data.
     * @return Output with data.
     * @throws StorageException Can't extract.
     */
    Output extract(Record record, String schema) throws StorageException;

    /**
     * Just execute some query and etc in database.
     * @param context Input parameters.
     * @param schema Schema.
     * @return Number of results.
     * @throws StorageException Can't extract.
     */
    int execute(Map<String, ?> context, String schema) throws StorageException;

    /**
     * Just execute some query and etc in database.
     * @param record Input parameters.
     * @param schema Schema.
     * @return Number of results.
     * @throws StorageException Can't extract.
     */
    int execute(Record record, String schema) throws StorageException;
}
