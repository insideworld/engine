//package insideworld.engine.entities.keeper;
//
//import com.google.common.collect.Lists;
//import com.google.common.collect.Maps;
//import insideworld.engine.entities.Entity;
//import insideworld.engine.entities.storages.StorageException;
//import java.lang.reflect.Modifier;
//import java.util.Collection;
//import java.util.List;
//import java.util.Map;
//import java.util.stream.Collectors;
//import javax.inject.Singleton;
//import org.apache.commons.lang3.tuple.Pair;
//import org.reflections.Reflections;
//import org.reflections.util.ConfigurationBuilder;
//
//@Singleton
//public class ClassLoaderEntitiesKeeper implements EntitiesKeeper {
//
//    private final Reflections reflections;
//
//    private final Implementations implementations;
//
//    public ClassLoaderEntitiesKeeper() {
//        this(Thread.currentThread().getContextClassLoader());
//    }
//
//    public ClassLoaderEntitiesKeeper(final ClassLoader loader) {
//        this.reflections = new Reflections(
//            new ConfigurationBuilder()
//                .addClassLoaders(loader)
//                .forPackage("", loader)
//        );
//        this.implementations = this.findImplementations();
//    }
//
//    public Class<? extends Entity> getImplementation(final Class<? extends Entity> type)
//        throws StorageException {
//        final Class<? extends Entity> impl;
//        if (this.implementations.getImplementations().containsKey(type)) {
//            impl = this.implementations.getImplementations().get(type);
//        } else if (this.isImplementation(type)) {
//            impl = type;
//        } else {
//            throw new StorageException(
//                String.format("Can't find implementation for type %s", type.getName()));
//        }
//        return impl;
//    }
//
//
//    private Implementations findImplementations() {
//        final var interfaces = this.reflections.getSubTypesOf(Entity.class)
//            .stream().filter(Class::isInterface).collect(Collectors.toList());
//        final Implementations result = new Implementations(interfaces.size());
//        for (final var type : interfaces) {
//            this.reflections.getSubTypesOf(type).stream()
//                .filter(this::isImplementation)
//                .findFirst()
//                .ifPresentOrElse(
//                    entity -> result.addImplementation(type, entity),
//                    () -> result.addNotFound(type)
//                );
//        }
//        return result;
//    }
//
//    private boolean isImplementation(final Class<? extends Entity> type) {
//        return !type.isInterface() && !Modifier.isAbstract(type.getModifiers());
//    }
//
//    private class Implementations {
//
//        private final Map<Class<? extends Entity>, Class<? extends Entity>> implementations;
//        private final Collection<Class<? extends Entity>> notfound;
//
//        public Implementations(final int size) {
//            this.implementations = Maps.newHashMapWithExpectedSize(size);
//            this.notfound = Lists.newLinkedList();
//        }
//
//        public Map<Class<? extends Entity>, Class<? extends Entity>> getImplementations() {
//            return this.implementations;
//        }
//
//        public Collection<Class<? extends Entity>> getNotfound() {
//            return this.notfound;
//        }
//
//        public void addImplementation(final Class<? extends Entity> intr,
//                                      final Class<? extends Entity> impl) {
//            this.implementations.put(intr, impl);
//        }
//
//        public void addNotFound(final Class<? extends Entity> intr) {
//            this.notfound.add(intr);
//        }
//    }
//
//}
