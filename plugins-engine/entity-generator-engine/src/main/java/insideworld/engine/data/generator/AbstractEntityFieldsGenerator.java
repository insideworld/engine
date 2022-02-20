package insideworld.engine.data.generator;

import io.quarkus.gizmo.ClassCreator;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.util.Collection;

public abstract class AbstractEntityFieldsGenerator<T extends EntityInfo> {

    public void createFields(final ClassCreator creator, final T info) {
        final PropertyDescriptor[] beans;
        try {
            beans = Introspector.getBeanInfo(info.getEntity())
                .getPropertyDescriptors();
        } catch (final IntrospectionException exp) {
            throw new RuntimeException(exp);
        }
        for (final PropertyDescriptor bean : beans) {
            if (!this.fieldAvailable(bean)) {
                continue;
            }
            for (final FieldGenerator<T> generator : this.createGenerators()) {
                if (generator.can(bean, info)) {
                    generator.generate(creator, bean, info);
                    break;
                }
            }
        }
    }

    private boolean fieldAvailable(final PropertyDescriptor bean) {
        final boolean available;
        if (bean.getReadMethod() != null) {
            available = !bean.getReadMethod().isDefault();
        } else if (bean.getWriteMethod() != null) {
            available = !bean.getWriteMethod().isDefault();
        } else {
            available = false;
        }
        return available;
    }

    protected abstract Collection<FieldGenerator<T>> createGenerators();

}
