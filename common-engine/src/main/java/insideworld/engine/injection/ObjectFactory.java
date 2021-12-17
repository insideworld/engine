package insideworld.engine.injection;

import javax.enterprise.util.TypeLiteral;

public interface ObjectFactory {

    <T> T createObject(Class<T> type, Object... args);

    <T> T createObject(TypeLiteral<T> type, Object... args);

}
