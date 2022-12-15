/*
 * Copyright (c) 2022 Anton Eliseev
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
 * associated documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute,
 * sublicense, and/or sell copies of the Software, and to permit persons to whom the Software
 * is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or
 * substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT
 * NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package insideworld.engine.frameworks.quarkus.extension.deployment;

import io.quarkus.arc.deployment.GeneratedBeanBuildItem;
import io.quarkus.bootstrap.classloading.MemoryClassPathElement;
import io.quarkus.bootstrap.classloading.QuarkusClassLoader;
import io.quarkus.deployment.annotations.BuildProducer;
import io.quarkus.deployment.annotations.BuildStep;
import io.quarkus.deployment.builditem.BytecodeTransformerBuildItem;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.Collections;
import java.util.Map;

/**
 * Deep dark magic.
 * Reload classes of bean resolver for properly work with generics.
 * CDI and Quarkus libs can't work if bean and injection point has generics with wildcards.
 * TODO: Rewrite to ASM.
 * @since 0.14.0
 */
public class ReloadResolversExtension {

    /**
     * This build step using to upload necessary classes to RUNTIME classloader.
     * Quarkus using BeanTypeAssignabilityRules to found required bean in runtime.
     * This method need to properly work of ObjectFactory.
     * Quarkus method for reloading is
     * io.quarkus.arc.impl.BeanTypeAssignabilityRules#parametersMatch
     * and it's using in runtime class loader.
     * @param transformer Producer for bytecode transformer.
     * @throws IOException Can't read class file.
     */
    @BuildStep
    public void reloadForRuntime(final BuildProducer<BytecodeTransformerBuildItem> transformer)
        throws IOException {
        final byte[] bytecode = this.read("BeanTypeAssignabilityRules.class");
        transformer.produce(new BytecodeTransformerBuildItem.Builder()
            .setClassToTransform("io.quarkus.arc.impl.BeanTypeAssignabilityRules")
            .setEager(true)
            .setCacheable(true)
            .setInputTransformer((name, bytes) -> bytecode).build()
        );
    }

    /**
     * This build step using to upload necessary classes to AUGMENTATION classloader.
     * Quarkus using BeanResolverImpl to found required bean before launch application.
     * This method need to properly work dependency injection.
     * Quarkus method for reloading is
     * io.quarkus.arc.processor.BeanResolverImpl#parametersMatch
     * and it's using in augmentation class loader.
     * Need to use reflection to set resettableElement in Augmentation classloader
     * because the classloader is no support memory class loading. It's a crutch.
     * @param transformer GenerateBeanBuildItem for launch this build step before starting bean
     * discovery.
     * @throws IOException Can't read file.
     * @throws NoSuchFieldException Reflection error.
     * @throws IllegalAccessException Reflection error.
     */
    @BuildStep
    public void reloadForAugmentation(final BuildProducer<GeneratedBeanBuildItem> transformer)
        throws IOException, NoSuchFieldException, IllegalAccessException {
        final QuarkusClassLoader classLoader =
            (QuarkusClassLoader) this.getClass().getClassLoader();
        final Field resettable = classLoader.getClass().getDeclaredField("resettableElement");
        resettable.setAccessible(true);
        resettable.set(
            classLoader,
            new MemoryClassPathElement(Collections.emptyMap(), true)
        );
        resettable.setAccessible(false);
        classLoader.reset(Collections.emptyMap(),
            Map.of(
            "io/quarkus/arc/processor/BeanResolverImpl.class",
            this.read("BeanResolverImpl.class")
        ));
    }

    /**
     * Read a class from resource dir.
     * @param file Filename.
     * @return Bytecode.
     * @throws IOException Can't read a file.
     */
    private byte[] read(final String file) throws IOException {
        final InputStream stream = this.getClass().getClassLoader()
            .getResourceAsStream(String.format("/insideworld/engine/quarkus/extension/deployment/%s", file));
        final byte[] bytes = stream.readAllBytes();
        stream.close();
        return bytes;
    }
}
