package insideworld.engine.actions.keeper;

import java.util.Collection;

/**
 * Reprasentation of array to keep information.
 *
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
     * @param alias Alias of record.
     * @return Empty row for next initialisation.
     */
    Record createRecord(String alias);

    /**
     * Merge rows.
     */
    void merge(Table table);

    void add(Record record);

    /**
     * Return collection of reccords.
     * @return
     */
    Collection<Record> records();

}
