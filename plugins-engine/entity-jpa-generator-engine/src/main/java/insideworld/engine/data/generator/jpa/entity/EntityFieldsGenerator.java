package insideworld.engine.data.generator.jpa.entity;

import com.google.common.collect.ImmutableList;
import insideworld.engine.data.generator.AbstractEntityFieldsGenerator;
import insideworld.engine.data.generator.FieldGenerator;
import insideworld.engine.data.generator.jpa.entity.fields.EntitiesFieldGenerator;
import insideworld.engine.data.generator.jpa.entity.fields.EntityFieldGenerator;
import insideworld.engine.data.generator.jpa.entity.fields.PrimitiveFieldGenerator;
import insideworld.engine.data.generator.jpa.entity.search.JpaInfo;
import insideworld.engine.entities.Entity;
import java.util.Collection;
import java.util.Map;

public class EntityFieldsGenerator extends AbstractEntityFieldsGenerator<JpaInfo> {

    private final Map<Class<? extends Entity>, JpaInfo> entities;

    public EntityFieldsGenerator(final Map<Class<? extends Entity>, JpaInfo> entities) {
        this.entities = entities;
    }

    @Override
    protected Collection<FieldGenerator<JpaInfo>> createGenerators() {
        return ImmutableList.of(
            new PrimitiveFieldGenerator(),
            new EntitiesFieldGenerator(this.entities),
            new EntityFieldGenerator(this.entities)
        );
    }
}
