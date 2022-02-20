package insideworld.engine.data.generator.inmemory.entity.fields;

import com.google.common.base.CaseFormat;
import insideworld.engine.data.generator.AbstractFieldGenerator;
import insideworld.engine.data.generator.inmemory.entity.search.InMemoryInfo;
import insideworld.engine.entities.Entity;
import io.quarkus.gizmo.AnnotationCreator;
import io.quarkus.gizmo.FieldCreator;
import java.beans.PropertyDescriptor;
import java.lang.reflect.ParameterizedType;
import java.util.Collection;

public class EntitiesFieldGenerator extends AbstractFieldGenerator<InMemoryInfo> {

    @Override
    public boolean can(final PropertyDescriptor bean, final InMemoryInfo info) {
        final Class<?> ret = this.propertyType(bean, info);
        return Collection.class.isAssignableFrom(ret)
            && Entity.class.isAssignableFrom(this.getGeneric(bean));
    }

    @Override
    protected void addAnnotations(
        final FieldCreator field, final PropertyDescriptor descriptor, final InMemoryInfo info) {
        //Nothing to add.
    }

    @Override
    protected String defineType(final PropertyDescriptor descriptor, final InMemoryInfo info) {
        return Collection.class.getName();
    }

    @Override
    protected String fieldSignature(final PropertyDescriptor descriptor) {
        return String.format("Ljava/util/Collection<L%s;>;",
            this.getGeneric(descriptor).getName().replace(".","/")
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
