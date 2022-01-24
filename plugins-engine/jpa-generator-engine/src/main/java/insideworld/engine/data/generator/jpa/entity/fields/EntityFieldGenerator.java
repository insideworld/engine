package insideworld.engine.data.generator.jpa.entity.fields;


import com.google.common.base.CaseFormat;
import com.google.common.base.Strings;
import insideworld.engine.data.generator.jpa.entity.search.JpaInfo;
import insideworld.engine.entities.Entity;
import io.quarkus.gizmo.ClassCreator;
import io.quarkus.gizmo.FieldCreator;
import java.beans.PropertyDescriptor;
import java.util.Map;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 * Generate an entity implementation.
 */
public class EntityFieldGenerator extends AbstractFieldGenerator {

    private final Map<Class<? extends Entity>, JpaInfo> implementations;

    public EntityFieldGenerator(final Map<Class<? extends Entity>, JpaInfo> implementations) {
        this.implementations = implementations;
    }

    @Override
    public boolean can(final PropertyDescriptor bean) {
        return Entity.class.isAssignableFrom(bean.getReadMethod().getReturnType());
    }

    @Override
    protected void addAnnotations(
        final FieldCreator field, final PropertyDescriptor descriptor, final JpaInfo info) {
        field.addAnnotation(ManyToOne.class);
        final String name =
            CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, descriptor.getName()) + "_id";
        field.addAnnotation(JoinColumn.class).addValue("name", name);
    }

    @Override
    protected String defineType(final PropertyDescriptor descriptor) {
        return this.implementations
            .get(descriptor.getReadMethod().getReturnType())
            .getImplementation();
    }

    @Override
    protected String fieldSignature(final PropertyDescriptor descriptor) {
        return null;
    }

    @Override
    protected String readSignature(PropertyDescriptor descriptor) {
        return null;
    }

    @Override
    protected String writeSignature(PropertyDescriptor descriptor) {
        return null;
    }


}
