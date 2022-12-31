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

package insideworld.engine.plugins.generator.action.input;

import static java.beans.Introspector.USE_ALL_BEANINFO;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import insideworld.engine.plugins.generator.base.reflection.Reflection;
import io.quarkus.gizmo.ClassOutput;
import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Map;
import org.apache.commons.lang3.tuple.Pair;

public class InputGenerator {

    private Reflection reflection;
    private ClassOutput output;

    public InputGenerator(final Reflection reflection, final ClassOutput output) {
        this.reflection = reflection;
        this.output = output;
    }

    public void generate() {
        this.find().forEach(this::generate);
    }

    private void generate(final Class<?> type) {
        Map<String, Some> methods = this.getMethods(type);
        System.out.println("qwe");
    }


    public Map<String, Some> getMethods(final Class<?> type) {
        Map<String, Some> methods = Maps.newHashMap();
        for (final Method method : type.getMethods()) {
            final String name = method.getName();
            if (name.startsWith("get")) {
                final String substring = name.substring(3);
                if (methods.containsKey(substring)) {
                    final Some some = methods.get(substring);
                    some.get = true;
                    //TODO: Add type validation.
                } else {
                    Some some = new Some();
                    some.name = substring;
                    some.get = true;
                    some.type = method.getReturnType();
                    methods.put(substring, some);
                }
            } else if (name.startsWith("set")) {
                final String substring = name.substring(3);
                if (methods.containsKey(substring)) {
                    final Some some = methods.get(substring);
                    some.set = true;
                    //TODO: Add type validation.
                } else {
                    Some some = new Some();
                    some.name = substring;
                    some.set = true;
                    some.type = method.getParameterTypes()[0];
                    methods.put(substring, some);
                }
            }
        }
        return methods;
    }

    private Collection<Class<?>> find() {
        return this.reflection.getAnnotatedClasses(GenerateInput.class);
    }

 public class Some {

        String name;

        boolean get;

        boolean set;

        Class<?> type;

 }
}
