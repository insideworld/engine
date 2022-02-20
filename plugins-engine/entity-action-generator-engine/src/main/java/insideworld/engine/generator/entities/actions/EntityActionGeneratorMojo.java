package insideworld.engine.generator.entities.actions;

import insideworld.engine.generator.AbstractGeneratorMojo;
import insideworld.engine.generator.entities.actions.delete.DeleteActionGenerator;
import insideworld.engine.generator.entities.actions.read.ReadActionGenerator;
import insideworld.engine.generator.entities.actions.write.WriteActionGenerator;
import insideworld.engine.generator.reflection.Reflection;
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
