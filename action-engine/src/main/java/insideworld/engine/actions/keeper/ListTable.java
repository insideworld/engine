/*
 * Copyright (c) 2022 Anton Eliseev
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package insideworld.engine.actions.keeper;

import com.google.common.collect.Lists;
import insideworld.engine.actions.ActionsTags;
import insideworld.engine.injection.ObjectFactory;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import org.apache.commons.lang3.StringUtils;

@Dependent
public class ListTable implements Table {

    private final List<Record> records = Lists.newLinkedList();
    private final ObjectFactory factory;

    @Inject
    public ListTable(final ObjectFactory factory) {
        this.factory = factory;
    }

    @Override
    public Record createRecord() {
        return this.createRecord(null);
    }

    /**
     * Can be
     * @return
     */
    @Override
    public Record createRecord(String alias) {
        final Record record = this.factory.createObject(this.recordType());
        if (StringUtils.isNotEmpty(alias)) {
            record.put(ActionsTags.ALIAS, alias);
        }
        this.records.add(record);
        return record;
    }

    /**
     *
     * @return
     */
    protected Class<? extends Record> recordType() {
        return Record.class;
    }

    @Override
    public void merge(final Table table) {
        this.records.addAll(table.records());
    }

    @Override
    public void add(final Record record) {
        this.records.add(record);
    }

    @Override
    public Collection<Record> records() {
        return this.records;
    }

    @Override
    public Iterator<Record> iterator() {
        return this.records.iterator();
    }
}
