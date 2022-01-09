package insideworld.engine.reflection;

import java.util.Collection;

public interface Reflection {

    <T> Collection<Class<? extends T>> getSubTypesOf(final Class<T> type);
}
