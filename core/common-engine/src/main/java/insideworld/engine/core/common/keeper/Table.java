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

package insideworld.engine.core.common.keeper;

import java.util.Collection;

/**
 * Interface to collect a lot of records.
 * @since 0.2.0
 */
public interface Table extends Iterable<Record> {

    /**
     * Create a new object of row for next filling.
     * @return Empty row for next initialisation.
     */
    Record createRecord();

    /**
     * Create a new object of row for next filling.
     * Alias is a tag which add to record.
     * You may use it manually.
     * @param alias Alias of record.
     * @return Empty row for next initialisation.
     */
    Record createRecord(String alias);

    /**
     * Merge another table to current one.
     * All record from input table will be added to this.
     * @param table Table to merge.
     */
    void merge(Table table);

    /**
     * Add record to current table.
     * @param record Record to add.
     */
    void add(Record record);

    /**
     * Return collection of records.
     *
     * Don't use it as getter because jackson try to parse it as bean with fields.
     * If they don't use
     *
     *
     * @return Collection of records.
     */
    Collection<Record> getRecords();

}
