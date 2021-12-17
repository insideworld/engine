package insideworld.engine.actions.keeper;

import insideworld.engine.actions.keeper.tags.Tag;
import java.io.Serializable;
import java.util.Map;
import java.util.Optional;

public interface Record extends Serializable {

    <T> T get(String key);

    <T> T get(Tag<T> tag);

    <T> Optional<T> getOptional(Tag<T> tag);

    boolean contains(String key);

    <T> boolean contains(Tag<T> tag);

    <T> void put(String key, T value);

    <T> void put(Tag<T> tag, T value);

    <T> void put(String key, T value, boolean replace);

    <T> void put(Tag<T> tag, T value, boolean replace);

    Map<String, ? super Object> values();

    <T> Optional<T> optional(final Tag<T> tag);

    /**
     * Clone tag between context.
     * Using for action to action execution.
     *
     * @param tag
     * @param to
     */
    <T> void cloneTag(final Tag<T> tag, final Record to);
}
