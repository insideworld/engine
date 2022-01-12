//package insideworld.engine.data.generator.jpa.temp;
//
//import com.google.common.collect.Lists;
//import insideworld.engine.data.jpa.AbstractEntity;
//import insideworld.engine.entities.generate.GenerateEntity;
//import io.quarkus.gizmo.*;
//import org.apache.commons.lang3.tuple.Pair;
//import org.reflections.Reflections;
//
//import javax.enterprise.context.Dependent;
//import javax.persistence.Entity;
//import javax.persistence.Table;
//import java.beans.IntrospectionException;
//import java.beans.Introspector;
//import java.beans.PropertyDescriptor;
//import java.util.ArrayList;
//import java.util.Collection;
//import java.util.Collections;
//import java.util.Map;
//
//public class EntityGenerator {
//
//    private final Reflections reflections;
//    private final ClassOutput output;
//    private final Map<Class<?>, String> generated;
//
//    public EntityGenerator(final Reflections reflections,
//                           final ClassOutput output,
//                           final Map<Class<?>, String> generated) {
//        this.reflections = reflections;
//        this.output = output;
//        this.generated = generated;
//    }
//
//    public void generate() {
//        final ArrayList<Class<?>> classes = Lists.newArrayList(this.findEntities());
//        for (final Class<?> entity : classes) {
//            this.generate(entity);
//        }
//    }
//
//    public String generate(Class<?> entity) {
//        final String name;
//        if (this.generated.containsKey(entity)) {
//            name = this.generated.get(entity) ;
//        } else {
//            name = this.name(entity);
//            this.generated.put(entity, name);
//            final ClassCreator creator = ClassCreator.builder()
//                .classOutput(this.output)
//                .className(name)
//                .superClass(AbstractEntity.class)
//                .interfaces(entity)
//                .build();
//            creator.addAnnotation(Dependent.class);
//            creator.addAnnotation(Entity.class);
//            AnnotationCreator annotationCreator = creator.addAnnotation(Table.class);
//            Table annotation = entity.getAnnotation(Table.class);
//            annotationCreator.addValue("name", annotation.name());
//            annotationCreator.addValue("schema", annotation.schema());
//            final PropertyDescriptor[] beans;
//            try {
//                beans = Introspector.getBeanInfo(entity).getPropertyDescriptors();
//            } catch (IntrospectionException e) {
//                throw new RuntimeException(e);
//            }
//            for (final PropertyDescriptor bean : beans) {
//                final Class<?> returnType = bean.getReadMethod().getReturnType();
//                final String type;
//                if (insideworld.engine.entities.Entity.class.isAssignableFrom(returnType)) {
//                    type = this.generate(returnType);
//                } else {
//                    type = returnType.getName();
//                }
//                this.createField(creator, bean, type, Collections.emptyMap());
//            }
//            creator.close();
//        }
//        return name;
//    }
//
//    private void createField(final ClassCreator creator,
//                             final PropertyDescriptor property,
//                             final String type,
//                             final Map<Class<?>, Collection<Pair<String, String>>> annotations) {
//
//        final FieldCreator field = creator.getFieldCreator(property.getName(), type);
//        for (final var annotation : annotations.entrySet()) {
//            final AnnotationCreator acreator = field.addAnnotation(annotation.getKey());
//            for (final var params : annotation.getValue()) {
//                acreator.addValue(params.getLeft(), params.getRight());
//            }
//        }
//        final MethodCreator get = creator.getMethodCreator(property.getReadMethod().getName(), type);
//        get.returnValue(get.readInstanceField(field.getFieldDescriptor(), get.getThis()));
//        get.close();
//        final MethodCreator set = creator.getMethodCreator(
//            property.getWriteMethod().getName(), void.class, property.getWriteMethod().getParameterTypes()[0]);
//        set.writeInstanceField(field.getFieldDescriptor(), set.getThis(), set.getMethodParam(0));
//        set.returnValue(null);
//        set.close();
//
//    }
//
//    private Collection<Class<?>> findEntities() {
//        return this.reflections.getTypesAnnotatedWith(GenerateEntity.class);
//    }
//
//    private String name(final Class<?> entity) {
//        return "insideworld.engine.entities.generated.jpa." + entity.getSimpleName();
//    }
//
//}
