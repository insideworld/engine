package insideworld.engine.quarkus;

import static io.quarkus.deployment.annotations.ExecutionTime.STATIC_INIT;
//import insideworld.engine.database.AbstractEntity;
import insideworld.engine.entities.Entity;
import insideworld.engine.entities.storages.Storage;
import insideworld.engine.quarkus.extension.EntityRecorder;
import insideworld.engine.quarkus.extension.MyEntity;
import insideworld.engine.quarkus.extension.MyStorage;
import io.quarkus.arc.deployment.GeneratedBeanBuildItem;
import io.quarkus.arc.deployment.GeneratedBeanGizmoAdaptor;
import io.quarkus.arc.deployment.SyntheticBeanBuildItem;
import io.quarkus.arc.processor.AsmUtilCopy;
import io.quarkus.deployment.annotations.BuildProducer;
import io.quarkus.deployment.annotations.BuildStep;
import io.quarkus.deployment.annotations.Record;
import io.quarkus.gizmo.ClassCreator;
import io.quarkus.gizmo.ClassOutput;
import io.quarkus.gizmo.FieldCreator;
import io.quarkus.gizmo.FieldDescriptor;
import io.quarkus.gizmo.MethodCreator;
import io.quarkus.gizmo.ResultHandle;
import java.lang.reflect.ParameterizedType;
import java.util.Collections;
import java.util.List;
import javax.enterprise.context.Dependent;
import javax.inject.Singleton;

public class EntityExtension {

//    @BuildStep
//    @Record(STATIC_INIT)
//    List<SyntheticBeanBu ildItem> syntheticBean(EntityRecorder recorder) {
//        return Collections.singletonList(SyntheticBeanBuildItem.configure(Entity.class)
//            .scope(Dependent.class)
//            .runtimeValue(recorder.createEntity())
//            .done());
//    }

    @BuildStep
    void generatedBean(BuildProducer<GeneratedBeanBuildItem> generatedBeans) {

        final ClassOutput output = new GeneratedBeanGizmoAdaptor(generatedBeans);
        final ClassCreator creator = ClassCreator.builder()
            .classOutput(output)
            .className("insideworld.engine.generated.MyEntityImpl")
//            .superClass(AbstractEntity.class)
            .interfaces(MyEntity.class)
            .build();
        creator.addAnnotation(Dependent.class);
        final FieldCreator message = creator.getFieldCreator("message", String.class);
        final MethodCreator getMessage = creator.getMethodCreator("getMessage", String.class);
        getMessage.returnValue(getMessage.readInstanceField(message.getFieldDescriptor(), getMessage.getThis()));
        getMessage.close();
        final MethodCreator setMessage = creator.getMethodCreator("setMessage", void.class, String.class);
        setMessage.writeInstanceField(message.getFieldDescriptor(), setMessage.getThis(), setMessage.getMethodParam(0));
        setMessage.returnValue(null);
        setMessage.close();
        creator.close();
//        final ClassCreator creator1

//        final String generatedSubClassSignature = AsmUtilCopy.getGeneratedSubClassSignature(Storage.class.getClassI, MyEntity.class);
        final ClassCreator creator1 = ClassCreator.builder()
            .classOutput(output)
            .className("insideworld.engine.generated.MyStorageImpl")
//            .superClass(AbstractEntity.class)
            .interfaces(Storage.class)
            .signature("Ljava/lang/Object;Linsideworld/engine/entities/storages/Storage<Linsideworld/engine/quarkus/extension/MyEntity;>;")
            .build();
        creator1.addAnnotation(Singleton.class);
        creator1.close();

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
}
