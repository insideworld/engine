package insideworld.engine.data.generator.jpa;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;
import insideworld.engine.data.generator.jpa.actions.read.ReadActionGenerator;
import insideworld.engine.data.generator.jpa.entity.EntityClassGenerator;
import insideworld.engine.data.generator.jpa.entity.EntityGenerator;
import insideworld.engine.data.generator.jpa.entity.search.JpaInfo;
import insideworld.engine.data.generator.jpa.entity.search.SearchEntities;
import insideworld.engine.data.generator.jpa.entity.search.SearchMixin;
import insideworld.engine.data.generator.jpa.storage.StorageGenerator;
import insideworld.engine.entities.Entity;
import insideworld.engine.reflection.ClassLoaderReflection;
import insideworld.engine.reflection.Reflection;
import io.quarkus.gizmo.ClassOutput;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.apache.maven.artifact.DependencyResolutionRequiredException;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;
import org.apache.maven.project.MavenProject;

@Mojo(name = "generate",
    defaultPhase = LifecyclePhase.GENERATE_SOURCES,
    requiresDependencyResolution = ResolutionScope.COMPILE
)
public class JpaGeneratorMojo extends AbstractMojo {

    @Parameter(defaultValue = "${project}")
    private MavenProject project;

    @Parameter(property = "scan", required = true)
    private String[] scan;

    @Parameter(property = "save", required = true)
    private String save;

    public void execute() throws MojoExecutionException {
        try {
            final ClassOutput output = new ClassWriter(this.project.getBuild().getOutputDirectory());
            final Reflection reflection = new ClassLoaderReflection(
                this.getClassLoader(this.project), this.scan);
            final EntityGenerator entities = new EntityGenerator(output, reflection, this.save);
            final Map<Class<? extends Entity>, String> generate = entities.findAndGenerate();
            final StorageGenerator storages = new StorageGenerator(
                output, reflection, this.save, generate
            );
            storages.generate();
            final ReadActionGenerator read = new ReadActionGenerator(reflection, output, this.save);
            read.generate();
        } catch (Exception exp) {
            throw new MojoExecutionException(exp);
        }
    }




//    private Collection<Class<?>> findEntities(final Reflections reflections) {
//        return reflections.getTypesAnnotatedWith(GenerateEntity.class);
//    }

//    private Collection<Class<?>> findStorages(final Reflections reflections) {
//        return reflections.getTypesAnnotatedWith(GenerateStorage.class);
//    }

    private ClassLoader getClassLoader(MavenProject project)
        throws DependencyResolutionRequiredException, MalformedURLException {
        final var classpathElements = project.getCompileClasspathElements();
        classpathElements.add(project.getBuild().getOutputDirectory());
        classpathElements.add(project.getBuild().getTestOutputDirectory());
        final URL[] urls = new URL[classpathElements.size()];
        for (int i = 0; i < classpathElements.size(); ++i) {
            urls[i] = new File(classpathElements.get(i)).toURI().toURL();
        }
        return new URLClassLoader(urls, this.getClass().getClassLoader());

    }
}
