package insideworld.engine.generator.reflection;

import insideworld.engine.properties.PropertiesException;
import insideworld.engine.properties.PropertiesProvider;
import java.util.Collection;
import javax.inject.Inject;
import javax.inject.Singleton;
import org.reflections.Reflections;
import org.reflections.util.ConfigurationBuilder;

public class ClassLoaderReflection implements Reflection {

    private final Reflections reflections;

//    @Inject
//    public ClassLoaderReflection(final PropertiesProvider provider) throws PropertiesException {
//        this(
//            Thread.currentThread().getContextClassLoader(),
//            provider.provide("engine.package", String.class),
//            "insideworld.engine"
//        );
//    }

    public ClassLoaderReflection(final ClassLoader loader, final String... packages) {
        final ConfigurationBuilder builder = new ConfigurationBuilder().addClassLoaders(loader);
        for (final String pack : packages) {
            builder.forPackage(pack, loader);
        }
        this.reflections = new Reflections(builder);
    }

    public <T> Collection<Class<? extends T>> getSubTypesOf(final Class<T> type) {
        return this.reflections.getSubTypesOf(type);
    }
}
