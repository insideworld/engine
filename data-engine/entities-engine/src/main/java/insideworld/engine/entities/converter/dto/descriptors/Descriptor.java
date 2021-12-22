package insideworld.engine.entities.converter.dto.descriptors;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class Descriptor {

    private final String name;
    private final Class<?> type;
    private final Type parameterized;
    private final Method method;

    public Descriptor(
        final String name,
        final Class<?> type,
        final Type parameterized,
        final Method method) {
        this.name = name;
        this.type = type;
        this.parameterized = parameterized;
        this.method = method;
    }

    public String getName() {
        return this.name;
    }

    public Class<?> getType() {
        return this.type;
    }

    public Type getParameterized() {
        return this.parameterized;
    }

    public Method getMethod() {
        return this.method;
    }

}
