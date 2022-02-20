package insideworld.engine.generator;

import insideworld.engine.generator.reflection.ClassLoaderReflection;
import insideworld.engine.generator.reflection.Reflection;
import io.quarkus.gizmo.ClassOutput;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import org.apache.maven.artifact.DependencyResolutionRequiredException;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

public abstract class AbstractGeneratorMojo extends AbstractMojo {

    @Parameter(defaultValue = "${project}")
    private MavenProject project;

    @Parameter(property = "scan", required = true)
    private String[] scan;

    public void execute() throws MojoExecutionException {
        try {
            final ClassOutput output = new ClassWriter(this.project.getBuild().getOutputDirectory());
            final Reflection reflection = new ClassLoaderReflection(
                this.getClassLoader(this.project), this.scan);
            this.generate(reflection, output);
        } catch (Exception exp) {
            throw new MojoExecutionException(exp);
        }
    }

    protected abstract void generate(final Reflection reflection, final ClassOutput output);

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
