package insideworld.engine.data.generator.inmemory.entity;

import insideworld.engine.data.generator.inmemory.entity.abstracts.AbstractMemoryEntity;
import insideworld.engine.data.generator.inmemory.entity.search.InMemoryInfo;
import io.quarkus.gizmo.ClassCreator;
import io.quarkus.gizmo.ClassOutput;
import javax.enterprise.context.Dependent;

public class InMemoryEntityClassGenerator {

    private final ClassOutput output;

    public InMemoryEntityClassGenerator(final ClassOutput output) {
        this.output = output;
    }

    public ClassCreator createEntity(final InMemoryInfo info) {
        final ClassCreator creator = ClassCreator.builder()
            .classOutput(this.output)
            .className(info.getImplementation())
            .superClass(AbstractMemoryEntity.class)
            .interfaces(info.getEntity())
            .build();
        creator.addAnnotation(Dependent.class);
        return creator;
    }

}
