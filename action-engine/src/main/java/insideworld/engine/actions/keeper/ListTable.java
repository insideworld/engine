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
