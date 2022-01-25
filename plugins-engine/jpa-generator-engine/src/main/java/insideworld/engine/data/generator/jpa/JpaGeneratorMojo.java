package insideworld.engine.data.generator.jpa;

import insideworld.engine.data.generator.jpa.entity.EntityGenerator;
import insideworld.engine.data.generator.jpa.entity.search.JpaInfo;
import insideworld.engine.data.generator.jpa.storage.StorageGenerator;
import insideworld.engine.entities.Entity;
import insideworld.engine.generator.AbstractGeneratorMojo;
import insideworld.engine.reflection.Reflection;
import io.quarkus.gizmo.ClassOutput;
import java.util.Map;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.ResolutionScope;

@Mojo(name = "generate",
    defaultPhase = LifecyclePhase.PROCESS_CLASSES,
    requiresDependencyResolution = ResolutionScope.COMPILE
)
public class JpaGeneratorMojo extends AbstractGeneratorMojo {

    @Override
    protected void generate(Reflection reflection, ClassOutput output) {
        final Map<Class<? extends Entity>, JpaInfo> generate =
            new EntityGenerator(output, reflection).findAndGenerate();
        new StorageGenerator(output, reflection, generate)
            .generate();
    }
}
