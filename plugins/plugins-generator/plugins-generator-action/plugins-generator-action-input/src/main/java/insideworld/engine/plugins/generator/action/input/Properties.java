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

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;

/**
 * Class to find all getter and setters in provided interface.
 *
 * @since 2.0.0
 */
public class Properties {

    public static final String GET = "get";

    public static final String SET = "set";

    private final Class<?> type;

    public Properties(final Class<?> type) {
        this.type = type;
    }

    public Collection<Property> findProperties(final StringBuilder builder) {
        final StringBuilder invalid = new StringBuilder();
        final Map<String, List<Method>> grouped = Arrays.stream(this.type.getMethods())
            .filter(this::checkName)
            .collect(
                Collectors.groupingBy(value -> value.getName().substring(3))
            );
        final List<Property> properties = grouped.entrySet().stream().map(
            entry -> {
                var property = new Property(entry.getKey(), entry.getValue());
                property.init(invalid);
                return property;
            }).toList();
        if (!invalid.isEmpty()) {
            builder.append("Problems with class ").append(this.type.getName()).append("\n\r");
            builder.append(invalid);
        }
        return properties;
    }

    /**
     * Check that name more than 4 letters and has prefix.
     *
     * @param method Method.
     * @return True is more than 4 letters and has prefix, else - false.
     */
    private boolean checkName(final Method method) {
        final String name = method.getName();
        return name.length() > 4 && (name.startsWith(GET) || name.startsWith(SET));
    }

}
