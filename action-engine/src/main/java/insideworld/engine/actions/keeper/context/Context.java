package insideworld.engine.actions.keeper.context;


import insideworld.engine.actions.keeper.Record;
import insideworld.engine.actions.keeper.tags.Tag;

/**
 * Interface marker using for mark dataset like input.
 *
 * Using for understand which kind of data income from external system.
 *
 * @since 0.0.1
 */
public interface Context extends Record {

    /**
     * Clone with included tags.
     * @param includes
     * @return
     */
    Context clone(Tag<?>... includes);

    /**
     * Clone all without system tags.
     * @return Cloned context.
     */
    Context clone();

    /**
     * Clone from record.
     * @param record Record to clone.
     * @return Clone context.
     */
    Context clone(Record record);
}
