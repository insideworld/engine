package insideworld.engine.quarkus;

import insideworld.engine.injection.ObjectFactory;
import javax.enterprise.inject.Instance;
import javax.enterprise.inject.spi.CDI;
import javax.enterprise.util.TypeLiteral;
import javax.inject.Singleton;

@Singleton
public class CDIObjectFactory implements ObjectFactory {

    @Override
    public <T> T createObject(final Class<T> type, final Object... args) {
        return CDI.current().select(type).get();
    }

    @Override
    public <T> T createObject(final TypeLiteral<T> type, final Object... args) {
        return CDI.current().select(type).get();
    }
}
