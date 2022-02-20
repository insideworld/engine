package insideworld.engine.generator.reflection;

import java.util.Collection;

public interface Reflection {

    <T> Collection<Class<? extends T>> getSubTypesOf(final Class<T> type);
}
