package insideworld.engine.data.generator.jpa;

import io.quarkus.gizmo.ClassOutput;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.OpenOption;

public class ClassWriter implements ClassOutput {

    private final String path;

    public ClassWriter(final String path) {
        this.path = path;
    }

    @Override
    public void write(final String name, final byte[] data) {
        try {
//            File dir = new File(this.project.getBuild().getOutputDirectory(), name.substring(0, name.lastIndexOf("/")));
            final File dir = new File(this.path, name.substring(0, name.lastIndexOf("/")));
            dir.mkdirs();
            final File output = new File(this.path, name + ".class");
            Files.write(output.toPath(), data, new OpenOption[0]);
        } catch (final IOException exp) {
            throw new IllegalStateException(String.format("Can't create class %s", name), exp);
        }
    }

}
