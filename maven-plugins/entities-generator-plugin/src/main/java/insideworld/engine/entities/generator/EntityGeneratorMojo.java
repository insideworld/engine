package insideworld.engine.entities.generator;

import com.google.common.collect.Maps;
import insideworld.engine.database.AbstractCrudGenericStorage;
import insideworld.engine.database.AbstractEntity;
import insideworld.engine.entities.generate.GenerateEntity;
import insideworld.engine.entities.generate.GenerateStorage;
import io.quarkus.gizmo.AnnotationCreator;
import io.quarkus.gizmo.ClassCreator;
import io.quarkus.gizmo.ClassOutput;
import io.quarkus.gizmo.FieldCreator;
import io.quarkus.gizmo.MethodCreator;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.util.Map;
import javax.enterprise.context.Dependent;
import javax.inject.Singleton;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;
import org.apache.maven.project.MavenProject;
import org.reflections.Reflections;
import org.reflections.Store;
import org.reflections.util.ConfigurationBuilder;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Collection;
import org.reflections.util.QueryFunction;

import static org.reflections.scanners.Scanners.SubTypes;
import static org.reflections.scanners.Scanners.TypesAnnotated;

@Mojo(name = "sayhi",
    defaultPhase = LifecyclePhase.GENERATE_SOURCES,
    requiresDependencyResolution = ResolutionScope.COMPILE
)
public class EntityGeneratorMojo extends AbstractMojo {

    @Parameter(defaultValue = "${project}")
    private MavenProject project;

    public void execute() throws MojoExecutionException {
        final ClassLoader loader = this.getClassLoader(this.project);
        final var reflections = new Reflections(
            new ConfigurationBuilder()
                .addClassLoaders(loader)
                .forPackage("insideworld", loader)
        );
//            new ConfigurationBuilder().addClassLoaders(this.getClassLoader(this.project))
//                .forPackage("insideworld"));
        final Collection<Class<?>> entities = this.findEntities(reflections);
        final Collection<Class<?>> storages = this.findStorages(reflections);
        System.out.println("qwe");
        final Map<Class<?>, String> created = Maps.newHashMap();
        final ClassOutput output = new EntityWriter(this.project);
        for (final Class<?> entity : entities) {
            created.put(entity, "insideworld.engine.entities.generated.jpa." + entity.getSimpleName());
            final ClassCreator creator = ClassCreator.builder()
                .classOutput(output)
                .className("insideworld.engine.entities.generated.jpa." + entity.getSimpleName())
                .superClass(AbstractEntity.class)
                .interfaces(entity)
                .build();
            creator.addAnnotation(Dependent.class);
            creator.addAnnotation(Entity.class);
            AnnotationCreator annotationCreator = creator.addAnnotation(Table.class);
            Table annotation = entity.getAnnotation(Table.class);
            annotationCreator.addValue("name", annotation.name());
            annotationCreator.addValue("schema", annotation.schema());

            final PropertyDescriptor[] beans;
            try {
                beans = Introspector.getBeanInfo(entity).getPropertyDescriptors();
            } catch (IntrospectionException e) {
                throw new RuntimeException(e);
            }
            for (final PropertyDescriptor bean : beans) {
                final FieldCreator message = creator.getFieldCreator(bean.getName(), bean.getReadMethod().getReturnType());
                message.addAnnotation(Column.class);
                final MethodCreator getMessage = creator.getMethodCreator(bean.getReadMethod().getName(), bean.getReadMethod().getReturnType());
                getMessage.returnValue(getMessage.readInstanceField(message.getFieldDescriptor(), getMessage.getThis()));
                getMessage.close();
                final MethodCreator setMessage = creator.getMethodCreator(bean.getWriteMethod().getName(), void.class, bean.getWriteMethod().getParameterTypes()[0]);
                setMessage.writeInstanceField(message.getFieldDescriptor(), setMessage.getThis(), setMessage.getMethodParam(0));
                setMessage.returnValue(null);
                setMessage.close();
            }
            creator.close();
        }

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
