package insideworld.engine.generator.actions;

import insideworld.engine.generator.AbstractGeneratorMojo;
import insideworld.engine.reflection.Reflection;
import io.quarkus.gizmo.ClassOutput;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.ResolutionScope;

@Mojo(name = "generate",
    defaultPhase = LifecyclePhase.PROCESS_CLASSES,
    requiresDependencyResolution = ResolutionScope.COMPILE
)
public class ActionGeneratorMojo extends AbstractGeneratorMojo {
    @Override
    protected void generate(final Reflection reflection, final ClassOutput output) {
        new LinkInputGenerator(reflection, output).generate();
    }
}
