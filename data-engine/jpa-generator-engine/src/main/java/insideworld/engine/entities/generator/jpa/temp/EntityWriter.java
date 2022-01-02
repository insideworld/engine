package insideworld.engine.entities.generator.jpa.temp;

import io.quarkus.gizmo.ClassOutput;
import org.apache.maven.project.MavenProject;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.OpenOption;

public class EntityWriter implements ClassOutput {

    private final MavenProject project;


    public EntityWriter(final MavenProject project) {
        this.project = project;
    }

    @Override
    public void write(String name, byte[] data) {
        System.out.println("qwe");
        try {
            File dir = new File(this.project.getBuild().getOutputDirectory(), name.substring(0, name.lastIndexOf("/")));
            dir.mkdirs();
            File output = new File(this.project.getBuild().getOutputDirectory(), name + ".class");
            Files.write(output.toPath(), data, new OpenOption[0]);
        } catch (IOException var4) {
            throw new IllegalStateException("Cannot dump the class: " + name, var4);
        }
        //        if (System.getProperty("dumpClass") != null) {
//        }
//
//        try {
//            this.visibleDefineClassMethod.invoke(this.classLoader, name.replace('/', '.'), data, 0, data.length);
//        } catch (InvocationTargetException | IllegalAccessException var4) {
//            throw new RuntimeException(var4);
//        }
    }


}
