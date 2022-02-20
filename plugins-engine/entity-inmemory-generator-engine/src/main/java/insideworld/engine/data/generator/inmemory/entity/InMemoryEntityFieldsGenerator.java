package insideworld.engine.data.generator.inmemory.entity;

import com.google.common.collect.ImmutableList;
import insideworld.engine.data.generator.AbstractEntityFieldsGenerator;
import insideworld.engine.data.generator.FieldGenerator;
import insideworld.engine.data.generator.inmemory.entity.fields.EntitiesFieldGenerator;
import insideworld.engine.data.generator.inmemory.entity.fields.EntityFieldGenerator;
import insideworld.engine.data.generator.inmemory.entity.fields.PrimitiveFieldGenerator;
import insideworld.engine.data.generator.inmemory.entity.search.InMemoryInfo;
import java.util.Collection;

public class InMemoryEntityFieldsGenerator extends AbstractEntityFieldsGenerator<InMemoryInfo> {

    @Override
    protected Collection<FieldGenerator<InMemoryInfo>> createGenerators() {
        return ImmutableList.of(
            new EntitiesFieldGenerator(),
            new EntityFieldGenerator(),
            new PrimitiveFieldGenerator()
        );
    }
}
