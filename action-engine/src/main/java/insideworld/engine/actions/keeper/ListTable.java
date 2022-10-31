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

package insideworld.engine.actions.keeper;

import com.google.common.collect.Lists;
import insideworld.engine.injection.ObjectFactory;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import org.apache.commons.lang3.StringUtils;

/**
 * Linked list implementation of Table class.
 * @since 0.1.0
 */
@Dependent
public class ListTable implements Table {

    /**
     * Records collection.
     */
    private final List<Record> records;

    /**
     * Object factory.
     */
    private final ObjectFactory factory;

    /**
     * Default constructor.
     * @param factory Object factory.
     */
    @Inject
    public ListTable(final ObjectFactory factory) {
        this.factory = factory;
        this.records = Lists.newLinkedList();
    }

    @Override
    public final Record createRecord() {
        return this.createRecord(null);
    }

    @Override
    public final Record createRecord(final String alias) {
        final Record record = this.factory.createObject(this.recordType());
        if (StringUtils.isNotEmpty(alias)) {
            record.put(KeeperTags.ALIAS, alias);
        }
        this.records.add(record);
        return record;
    }

    @Override
    public final void merge(final Table table) {
        this.records.addAll(table.getRecords());
    }

    @Override
    public final void add(final Record record) {
        this.records.add(record);
    }

    @Override
    public final Collection<Record> getRecords() {
        return this.records;
    }

    @Override
    public final Iterator<Record> iterator() {
        return this.records.iterator();
    }

    /**
     * Record type which need to use for create a new record.
     * @return Type of record.
     * @checkstyle NonStaticMethodCheck (2 lines)
     */
    protected Class<? extends Record> recordType() {
        return Record.class;
    }
}
