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
import java.util.stream.Collectors;
import javax.enterprise.context.Dependent;
import javax.persistence.Table;
import org.apache.commons.lang3.tuple.Pair;

public class EntityClassGenerator {

    private final ClassOutput output;

    public EntityClassGenerator(final ClassOutput output) {
        this.output = output;
    }

    public ClassCreator createEntity(final JpaInfo info) {
        final ClassCreator creator = ClassCreator.builder()
            .classOutput(this.output)
            .className(info.getImplementation())
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

}
