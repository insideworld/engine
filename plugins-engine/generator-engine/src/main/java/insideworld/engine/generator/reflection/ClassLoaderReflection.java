/*
 * Copyright (c) 2022 Anton Eliseev
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package insideworld.engine.generator.reflection;

import insideworld.engine.properties.PropertiesException;
import insideworld.engine.properties.PropertiesProvider;
import java.util.Collection;
import javax.inject.Inject;
import javax.inject.Singleton;
import org.reflections.Reflections;
import org.reflections.util.ConfigurationBuilder;

public class ClassLoaderReflection implements Reflection {

    private final Reflections reflections;

//    @Inject
//    public ClassLoaderReflection(final PropertiesProvider provider) throws PropertiesException {
//        this(
//            Thread.currentThread().getContextClassLoader(),
//            provider.provide("engine.package", String.class),
//            "insideworld.engine"
//        );
//    }

    public ClassLoaderReflection(final ClassLoader loader, final String... packages) {
        final ConfigurationBuilder builder = new ConfigurationBuilder().addClassLoaders(loader);
        for (final String pack : packages) {
            builder.forPackage(pack, loader);
        }
        this.reflections = new Reflections(builder);
    }

    public <T> Collection<Class<? extends T>> getSubTypesOf(final Class<T> type) {
        return this.reflections.getSubTypesOf(type);
    }
}
