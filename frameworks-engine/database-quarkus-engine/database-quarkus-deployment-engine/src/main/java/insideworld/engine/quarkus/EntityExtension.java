package insideworld.engine.quarkus;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;
import insideworld.engine.database.AbstractCrudGenericStorage;
import insideworld.engine.database.AbstractEntity;
import insideworld.engine.entities.generate.GenerateEntity;
import insideworld.engine.entities.generate.GenerateStorage;
import insideworld.engine.entities.storages.Storage;
import io.quarkus.arc.deployment.BeanArchiveIndexBuildItem;
import io.quarkus.arc.deployment.GeneratedBeanBuildItem;
import io.quarkus.arc.deployment.GeneratedBeanGizmoAdaptor;
import io.quarkus.deployment.annotations.BuildProducer;
import io.quarkus.deployment.annotations.BuildStep;
import io.quarkus.deployment.annotations.Produce;
import io.quarkus.deployment.builditem.AdditionalIndexedClassesBuildItem;
import io.quarkus.deployment.builditem.CombinedIndexBuildItem;
import io.quarkus.deployment.builditem.GeneratedClassBuildItem;
import io.quarkus.gizmo.*;
import io.quarkus.hibernate.orm.deployment.AdditionalJpaModelBuildItem;
import io.quarkus.hibernate.orm.deployment.JpaModelIndexBuildItem;
import io.quarkus.hibernate.orm.deployment.PersistenceUnitDescriptorBuildItem;
import org.jboss.jandex.AnnotationInstance;
import org.jboss.jandex.AnnotationTarget;
import org.jboss.jandex.AnnotationValue;
import org.jboss.jandex.DotName;
import org.reflections.Reflections;

import javax.enterprise.context.Dependent;
import javax.inject.Singleton;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.executable.ValidateOnExecution;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import static org.reflections.scanners.Scanners.*;

public class EntityExtension {

    //    @BuildStep
//    @Record(STATIC_INIT)
//    List<SyntheticBeanBu ildItem> syntheticBean(EntityRecorder recorder) {
//        return Collections.singletonList(SyntheticBeanBuildItem.configure(Entity.class)
//            .scope(Dependent.class)
//            .runtimeValue(recorder.createEntity())
//            .done());
//    }
    private Collection<Class<?>> findEntities() {
        Reflections reflections = new Reflections("insideworld");
        final var filter = SubTypes
                .of(SubTypes.of(TypesAnnotated.with(GenerateEntity.class))).asClass();
        return reflections.get(filter);
    }

    private Collection<Class<?>> findStorages() {
        Reflections reflections = new Reflections("insideworld");
        final var filter = SubTypes
                .of(SubTypes.of(TypesAnnotated.with(GenerateStorage.class))).asClass();
        return reflections.get(filter);
    }

    private String prepareStorageSignature(String entity, String jpa) {
        return new StringBuilder().append("L").append(AbstractCrudGenericStorage.class.getName().replace(".", "/"))
                .append("<")
                .append("L").append(entity.replace(".", "/")).append(";")
                .append("L").append(jpa.replace(".", "/")).append(";")
                .append(">;")
                .toString();
    }

    /**
     * В общем работает это так
     * Ты делаешь метода с аннотацией BILDStep
     * Далее он принимет различные билд айтемы
     * @param generatedBeans
     * @param qwe
     * @throws IntrospectionException
     */
    @BuildStep
    void generatedBean(BuildProducer<GeneratedBeanBuildItem> generatedBeans,
                       BuildProducer<AdditionalIndexedClassesBuildItem> qwe) throws IntrospectionException {
//qwe.produce(new AdditionalIndexedClassesBuildItem());
        final Collection<Class<?>> entities = this.findEntities();
        final Map<Class<?>, String> created = Maps.newHashMap();
        final ClassOutput output = new GeneratedBeanGizmoAdaptor(generatedBeans);
        for (final Class<?> entity : entities) {
            created.put(entity, "insideworld.engine.entities.generated.jpa." + entity.getSimpleName());
            final ClassCreator creator = ClassCreator.builder()
                    .classOutput(output)
                    .className("insideworld.engine.entities.generated.jpa." + entity.getSimpleName())
                    .superClass(AbstractEntity.class)
                    .interfaces(entity)
                    .build();

            creator.addAnnotation(Dependent.class);
            creator.addAnnotation(Entity.class);
            AnnotationCreator annotationCreator = creator.addAnnotation(Table.class);
            Table annotation = entity.getAnnotation(Table.class);
            annotationCreator.addValue("name", annotation.name());
            annotationCreator.addValue("schema", annotation.schema());

            final var beans = Introspector.getBeanInfo(entity).getPropertyDescriptors();
            for (final PropertyDescriptor bean : beans) {
                final FieldCreator message = creator.getFieldCreator(bean.getName(), Object.class);
                message.addAnnotation(Column.class);
                final MethodCreator getMessage = creator.getMethodCreator(bean.getReadMethod().getName(), bean.getReadMethod().getReturnType());
                getMessage.returnValue(getMessage.readInstanceField(message.getFieldDescriptor(), getMessage.getThis()));
                getMessage.close();
                final MethodCreator setMessage = creator.getMethodCreator(bean.getWriteMethod().getName(), void.class, bean.getWriteMethod().getParameterTypes()[0]);
                setMessage.writeInstanceField(message.getFieldDescriptor(), setMessage.getThis(), setMessage.getMethodParam(0));
                setMessage.returnValue(null);
                setMessage.close();
            }
            creator.close();
        }

        for (final Class<?> storage : this.findStorages()) {
            final ClassCreator creator1 = ClassCreator.builder()
                    .classOutput(output)
                    .className("insideworld.engine.entities.generated.storage." + storage.getSimpleName() + "Storage")
                    .superClass(AbstractCrudGenericStorage.class)
                    .signature(this.prepareStorageSignature(storage.getName(), created.get(storage)))
                    .build();
            creator1.addAnnotation(Singleton.class);
            creator1.close();
        }



//
//        final ClassCreator creator = ClassCreator.builder()
//                .classOutput(output)
//                .className("insideworld.engine.generated.MyEntityImpl")
////            .superClass(AbstractEntity.class)
//                .interfaces(MyEntity.class)
//                .build();
//        creator.addAnnotation(Dependent.class);
//        final FieldCreator message = creator.getFieldCreator("message", String.class);
//        final MethodCreator getMessage = creator.getMethodCreator("getMessage", String.class);
//        getMessage.returnValue(getMessage.readInstanceField(message.getFieldDescriptor(), getMessage.getThis()));
//        getMessage.close();
//        final MethodCreator setMessage = creator.getMethodCreator("setMessage", void.class, String.class);
//        setMessage.writeInstanceField(message.getFieldDescriptor(), setMessage.getThis(), setMessage.getMethodParam(0));
//        setMessage.returnValue(null);
//        setMessage.close();
//        creator.close();
//        final ClassCreator creator1

//        final String generatedSubClassSignature = AsmUtilCopy.getGeneratedSubClassSignature(Storage.class.getClassI, MyEntity.class);
//        final ClassCreator creator1 = ClassCreator.builder()
//            .classOutput(output)
//            .className("insideworld.engine.generated.MyStorageImpl")
////            .superClass(AbstractEntity.class)
//            .interfaces(Storage.class)
//            .signature("Ljava/lang/Object;Linsideworld/engine/entities/storages/Storage<Linsideworld/engine/quarkus/extension/MyEntity;>;")
//            .build();
//        creator1.addAnnotation(Singleton.class);
//        creator1.close();

//        creator.
//        final ResultHandle resultHandle = getMessage.readInstanceField(message.getFieldDescriptor(), getMessage.getThis());
//        getMessage.close();
//        System.out.println(resultHandle);
//        getMessage.load()

//        final FieldDescriptor fieldDescriptor = message.getFieldDescriptor();
//        final MethodCreator setMessage = creator.getMethodCreator("setMessage", String.class);
//        setMessage.writeInstanceField(fieldDescriptor, setMessage.getThis(), setMessage.getMethodParam(0));
//        final MethodCreator getMessage = creator.getMethodCreator("getMessage", String.class, void.class);
//        getMessage.readInstanceField(fieldDescriptor, getMessage.getThis());
//        creator.close();
//        setMessage.
//
//        creator.close();
//        FieldDescriptor fieldDesc = cc.getFieldCreator(configName, List.class).setModifiers(Modifier.PRIVATE).getFieldDescriptor();
//        MethodCreator getter = cc.getMethodCreator(JavaBeanUtil.getGetterName(configName, classInfo.name()), List.class);
//        getter.setSignature(String.format("()Ljava/util/List<L%s;>;", forSignature(classInfo)));
//        getterDesc = getter.getMethodDescriptor();
//        getter.returnValue(getter.readInstanceField(fieldDesc, getter.getThis()));
//        MethodCreator setter = cc.getMethodCreator(JavaBeanUtil.getSetterName(configName), void.class, List.class);
//        setter.setSignature(String.format("(Ljava/util/List<L%s;>;)V", forSignature(classInfo)));
//        setter.writeInstanceField(fieldDesc, setter.getThis(), setter.getMethodParam(0));
//        setter.returnValue(null);
    }

//    @BuildStep
//    public void ValidateOnExecution(BuildProducer<AdditionalJpaModelBuildItem> qwe,
//                                    List<GeneratedClassBuildItem> ewq) {
//        System.out.println(qwe);
////        qwe.produce(new AdditionalJpaModelBuildItem("insideworld.engine.entities.generated.jpa.MyEntity"));
//
//    }
}