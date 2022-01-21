package insideworld.engine.data.generator.jpa.entity.search;//package insideworld.engine.entities.generator.jpa.search;
//
//import insideworld.engine.entities.Entity;
//import org.reflections.Reflections;
//
//import java.lang.reflect.Modifier;
//import java.util.Collection;
//import java.util.Map;
//import java.util.stream.Collectors;
//
//public class SearchExists implements SearchEntities {
//
//    private final Reflections reflection;
//
//    public SearchExists(final Reflections reflection) {
//        this.reflection = reflection;
//    }
//
//    @Override
//    public void search(final Map<Class<? extends Entity>, String> entities) {
//        for (final Class<? extends Entity> type : this.findInterfaces()) {
//            this.reflection.getSubTypesOf(type).stream().filter(
//                    entity -> !entity.isInterface() && !Modifier.isAbstract(entity.getModifiers())
//            ).findFirst().ifPresent(entity -> entities.put(type, entity.getName()));
//        }
//    }
//
//    private Collection<Class<? extends Entity>> findInterfaces() {
//        return this.reflection.getSubTypesOf(Entity.class).stream()
//                .filter(Class::isInterface).collect(Collectors.toList());
//    }
//
//}
