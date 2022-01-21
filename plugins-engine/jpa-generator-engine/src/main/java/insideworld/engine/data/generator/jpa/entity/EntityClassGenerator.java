package insideworld.engine.data.generator.jpa.entity;

import com.google.common.collect.Maps;
import insideworld.engine.data.generator.jpa.entity.search.JpaInfo;
import insideworld.engine.data.jpa.AbstractEntity;
import insideworld.engine.entities.Entity;
import io.quarkus.gizmo.AnnotationCreator;
import io.quarkus.gizmo.ClassCreator;
import io.quarkus.gizmo.ClassOutput;
import java.util.Collection;
import java.util.Map;
import javax.enterprise.context.Dependent;
import javax.persistence.Table;

public class EntityClassGenerator {

    private final ClassOutput output;
    private final Collection<JpaInfo> infos;
    private final String packages;

    public EntityClassGenerator(final ClassOutput output,
                                final Collection<JpaInfo> infos,
                                final String packages) {
        this.output = output;
        this.infos = infos;
        this.packages = packages + ".generated.jpa.%s";
    }

    public Map<Class<? extends Entity>, ClassCreator> generate() {
        final Map<Class<? extends Entity>, ClassCreator> result =
            Maps.newHashMapWithExpectedSize(this.infos.size());
        infos.forEach(info -> result.put(info.getEntity(), this.createEntity(info)));
        return result;
    }

    private ClassCreator createEntity(final JpaInfo info) {
        final ClassCreator creator = ClassCreator.builder()
            .classOutput(this.output)
            .className(this.name(info.getEntity()))
            .superClass(AbstractEntity.class)
            .interfaces(info.getEntity())
            .build();
        creator.addAnnotation(Dependent.class);
        creator.addAnnotation(javax.persistence.Entity.class);
        AnnotationCreator annotationCreator = creator.addAnnotation(Table.class);
        annotationCreator.addValue("name", info.getTable());
        annotationCreator.addValue("schema", info.getSchema());
        return creator;
    }

    private String name(final Class<?> entity) {
        return String.format(this.packages, entity.getSimpleName());
//        return "insideworld.engine.entities.generated.jpa." + entity.getSimpleName();
    }

}
