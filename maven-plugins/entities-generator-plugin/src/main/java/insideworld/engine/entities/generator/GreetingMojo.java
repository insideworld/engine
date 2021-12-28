package insideworld.engine.entities.generator;

import insideworld.engine.database.AbstractCrudGenericStorage;
import insideworld.engine.entities.Entity;
import insideworld.engine.entities.generate.GenerateEntity;
import insideworld.engine.entities.generate.GenerateStorage;
import org.apache.maven.artifact.DependencyResolutionRequiredException;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;
import org.apache.maven.project.MavenProject;
import org.reflections.Reflections;
import org.reflections.util.ConfigurationBuilder;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.reflections.scanners.Scanners.SubTypes;
import static org.reflections.scanners.Scanners.TypesAnnotated;

@Mojo(name = "sayhi", requiresDependencyResolution = ResolutionScope.COMPILE)
public class GreetingMojo extends AbstractMojo {

    @Parameter(defaultValue = "${project}")
    private MavenProject project;

    private Reflections reflections;

    public void execute() throws MojoExecutionException {
        final var reflections = new Reflections(new ConfigurationBuilder().addClassLoaders(this.getClassLoader(this.project))
                .forPackage("insideworld"));

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
        final var filter = SubTypes
                .of(SubTypes.of(TypesAnnotated.with(GenerateEntity.class))).asClass();
        return reflections.get(filter);
    }

    private Collection<Class<?>> findStorages(final Reflections reflections) {
        final var filter = SubTypes
                .of(SubTypes.of(TypesAnnotated.with(GenerateStorage.class))).asClass();
        return reflections.get(filter);
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
