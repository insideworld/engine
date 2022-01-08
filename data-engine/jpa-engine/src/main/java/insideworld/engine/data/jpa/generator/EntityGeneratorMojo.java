package insideworld.engine.data.jpa.generator;

import com.google.common.collect.Maps;
import insideworld.engine.data.jpa.AbstractCrudGenericStorage;
import insideworld.engine.entities.generate.GenerateEntity;
import insideworld.engine.entities.generate.GenerateStorage;
import io.quarkus.gizmo.ClassCreator;
import io.quarkus.gizmo.ClassOutput;
import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Collection;
import java.util.Map;
import javax.inject.Singleton;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;
import org.apache.maven.project.MavenProject;
import org.reflections.Reflections;
import org.reflections.util.ConfigurationBuilder;

@Mojo(name = "sayhi",
    defaultPhase = LifecyclePhase.GENERATE_SOURCES,
    requiresDependencyResolution = ResolutionScope.COMPILE
)
public class EntityGeneratorMojo extends AbstractMojo {

    @Parameter(defaultValue = "${project}")
    private MavenProject project;

    public void execute() throws MojoExecutionException {
        final ClassOutput output = new EntityWriter(this.project);
        final ClassLoader loader = this.getClassLoader(this.project);
        final var reflections = new Reflections(
            new ConfigurationBuilder()
                .addClassLoaders(loader)
                .forPackage("insideworld", loader)
        );
//            new ConfigurationBuilder().addClassLoaders(this.getClassLoader(this.project))
//                .forPackage("insideworld"));
//        final Collection<Class<?>> entities = this.findEntities(reflections);
        final Collection<Class<?>> storages = this.findStorages(reflections);
        System.out.println("qwe");
        final Map<Class<?>, String> created = Maps.newHashMap();
        final EntityGenerator entityGenerator = new EntityGenerator(reflections, output, created);
        entityGenerator.generate();

        for (final Class<?> storage : storages) {
            final ClassCreator creator1 = ClassCreator.builder()
                .classOutput(output)
                .className("insideworld.engine.entities.generated.storage." + storage.getSimpleName() + "Storage")
                .superClass(AbstractCrudGenericStorage.class)
                .signature(this.prepareStorageSignature(storage.getName(), created.get(storage)))
                .build();
            creator1.addAnnotation(Singleton.class);
            creator1.close();
        }


    }

    private String prepareStorageSignature(final String entity, final String jpa) {
        return new StringBuilder().append("L").append(AbstractCrudGenericStorage.class.getName().replace(".", "/"))
                .append("<")
                .append("L").append(entity.replace(".", "/")).append(";")
                .append("L").append(jpa.replace(".", "/")).append(";")
                .append(">;")
                .toString();
    }

    private Collection<Class<?>> findEntities(final Reflections reflections) {
        return reflections.getTypesAnnotatedWith(GenerateEntity.class);
    }

    private Collection<Class<?>> findStorages(final Reflections reflections) {
        return reflections.getTypesAnnotatedWith(GenerateStorage.class);
    }

    private ClassLoader getClassLoader(MavenProject project) {
        try {
            final var classpathElements = project.getCompileClasspathElements();
            classpathElements.add(project.getBuild().getOutputDirectory());
            classpathElements.add(project.getBuild().getTestOutputDirectory());
            final URL[] urls = new URL[classpathElements.size()];
            for (int i = 0; i < classpathElements.size(); ++i) {
                urls[i] = new File(classpathElements.get(i)).toURI().toURL();
            }
            return new URLClassLoader(urls, this.getClass().getClassLoader());
        } catch (Exception e) {
            getLog().debug("Couldn't get the classloader.");
            return this.getClass().getClassLoader();
        }
    }

}
