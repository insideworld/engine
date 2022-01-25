package insideworld.engine.generator.actions.entities;

import insideworld.engine.generator.AbstractGeneratorMojo;
import insideworld.engine.generator.actions.entities.delete.DeleteActionGenerator;
import insideworld.engine.generator.actions.entities.read.ReadActionGenerator;
import insideworld.engine.generator.actions.entities.write.WriteActionGenerator;
import insideworld.engine.reflection.Reflection;
import io.quarkus.gizmo.ClassOutput;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.ResolutionScope;

@Mojo(name = "generate",
    defaultPhase = LifecyclePhase.PROCESS_CLASSES,
    requiresDependencyResolution = ResolutionScope.COMPILE
)
public class EntityActionGeneratorMojo extends AbstractGeneratorMojo {

    @Override
    protected void generate(final Reflection reflection, final ClassOutput output) {
        new ReadActionGenerator(reflection, output).generate();
        new WriteActionGenerator(reflection, output).generate();
        new DeleteActionGenerator(reflection, output).generate();
    }

}
