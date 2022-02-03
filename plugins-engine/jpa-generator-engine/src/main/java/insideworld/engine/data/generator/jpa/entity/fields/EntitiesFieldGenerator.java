package insideworld.engine.data.generator.jpa.entity.fields;

import com.google.common.base.CaseFormat;
import insideworld.engine.data.generator.jpa.entity.search.JpaInfo;
import insideworld.engine.entities.Entity;
import insideworld.engine.entities.converter.dto.descriptors.Descriptor;
import io.quarkus.arc.impl.ParameterizedTypeImpl;
import io.quarkus.gizmo.AnnotationCreator;
import io.quarkus.gizmo.ClassCreator;
import io.quarkus.gizmo.DescriptorUtils;
import io.quarkus.gizmo.FieldCreator;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import javax.persistence.OneToMany;
import org.jboss.jandex.DotName;
import org.jboss.jandex.Type;

public class EntitiesFieldGenerator extends AbstractFieldGenerator {

    private final Map<Class<? extends Entity>, JpaInfo> implementations;

    public EntitiesFieldGenerator(final Map<Class<? extends Entity>, JpaInfo> implementations) {
        this.implementations = implementations;
    }

    @Override
    public boolean can(final PropertyDescriptor bean, final JpaInfo info) {
        final Class<?> ret = this.propertyType(bean, info);
        return Collection.class.isAssignableFrom(ret)
            && Entity.class.isAssignableFrom(this.getGeneric(bean));
    }


    @Override
    protected void addAnnotations(
        final FieldCreator field, final PropertyDescriptor descriptor, final JpaInfo info) {
        final AnnotationCreator annotation = field.addAnnotation(OneToMany.class);
        final String name =
            CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, info.getTable() + "_id");
        annotation.addValue("mappedBy", name);
    }

    @Override
    protected String defineType(final PropertyDescriptor descriptor, final JpaInfo info) {
        return Collection.class.getName();
    }

    @Override
    protected String fieldSignature(final PropertyDescriptor descriptor) {
        final Class<?> generic = this.getGeneric(descriptor);
        return String.format("Ljava/util/Collection<L%s;>;",
            this.implementations.get(generic).getImplementation().replace(".","/")
        );
    }

    @Override
    protected String readSignature(final PropertyDescriptor descriptor) {
        return String.format(
            "()Ljava/util/Collection<L%s;>;",
            this.getGeneric(descriptor).getName().replace(".","/")
        );
    }

    @Override
    protected String writeSignature(final PropertyDescriptor descriptor) {
        return String.format(
            "(Ljava/util/Collection<L%s;>;)V",
            this.getGeneric(descriptor).getName().replace(".","/")
        );
    }


    private Class<?> getGeneric(final PropertyDescriptor descriptor) {
        final Class<?> type;
        if (descriptor.getReadMethod() != null) {
            type = (Class<?>) ((ParameterizedType)
                descriptor.getReadMethod().getGenericReturnType()).getActualTypeArguments()[0];
        } else {
            type = (Class<?>) (((ParameterizedType)
                descriptor
                    .getWriteMethod().getGenericParameterTypes()[0]).getActualTypeArguments()[0]);
        }
        return type;
    }
}
