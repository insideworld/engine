/*
 * Copyright (c) 2022 Anton Eliseev
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

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
