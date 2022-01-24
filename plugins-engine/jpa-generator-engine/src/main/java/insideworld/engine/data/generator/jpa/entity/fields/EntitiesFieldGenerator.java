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
    protected void addAnnotations(
        final FieldCreator field, final PropertyDescriptor descriptor, final JpaInfo info) {
        final AnnotationCreator annotation = field.addAnnotation(OneToMany.class);
        final String name =
            CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, info.getTable() + "_id");
        annotation.addValue("mappedBy", name);
    }

    @Override
    protected String defineType(final PropertyDescriptor descriptor) {
        return Collection.class.getName();
    }

    @Override
    protected String signature(final PropertyDescriptor descriptor) {
        final Class<?> generic = this.getGeneric(descriptor.getReadMethod());
        return String.format("Ljava/util/Collection<L%s;>;",
            this.implementations.get(generic).getImplementation()
        );
    }

    @Override
    public boolean can(final PropertyDescriptor bean) {
        final Class<?> ret = bean.getReadMethod().getReturnType();
        return Collection.class.isAssignableFrom(ret)
            && Entity.class.isAssignableFrom(this.getGeneric(bean.getReadMethod()));
    }

    private Class<?> getGeneric(final Method method) {
        return (Class<?>) ((ParameterizedType) method.getGenericReturnType()).getActualTypeArguments()[0];
    }
}
