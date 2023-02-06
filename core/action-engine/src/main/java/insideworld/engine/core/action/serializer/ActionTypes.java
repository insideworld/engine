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

package insideworld.engine.core.action.serializer;

import insideworld.engine.core.action.Action;
import insideworld.engine.core.action.executor.ActionExecutor;
import insideworld.engine.core.action.executor.key.Key;
import insideworld.engine.core.common.serializer.types.Type;
import insideworld.engine.core.common.serializer.types.Types;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import javax.inject.Inject;
import javax.inject.Singleton;
import org.apache.commons.lang3.tuple.Pair;

@Singleton
public class ActionTypes implements Types {

    private final ActionExecutor executor;

    @Inject
    public ActionTypes(final ActionExecutor executor) {
        this.executor = executor;
    }

    @Override
    public Set<Type> getTypes() {
        final Collection<Action<?, ?>> actions = this.executor.getKeys().values();
        final Set<Type> types = new HashSet<>(actions.size() * 2);
        for (final Action<?, ?> action : actions) {
            final Method method = this.findMethod(action);
            types.add(new MethodType(method,0));
            types.add(new MethodType(method, 1));
        }
        return types;
    }

    /**
     * Input and outputs extracted type grouped by key.
     * @return Map where key is action key registered in system
     * and a pair of input and output extracted types as value.
     */
    public Map<Key<?,?>, Pair<Type, Type>> getInputOutputs() {
        return this.executor.getKeys().entrySet().stream().collect(
            Collectors.toMap(
                Map.Entry::getKey,
                entry -> {
                    final Method method = this.findMethod(entry.getValue());
                    return Pair.of(
                        new MethodType(method, 0),
                        new MethodType(method, 1)
                    );
                }
            )
        );
    }

    /**
     * Find a valid method.
     * Because inheritance from interface create a method with Object, Object arguments -
     * need to find first where one of argument not equals to Object.
     * @param action Action to find method.
     * @return Method with reflective information.
     */
    private Method findMethod(final Action<?, ?> action) {
        Method candidate = null;
        for (final Method method : action.getClass().getMethods()) {
            if ("types".equals(method.getName())) {
                candidate = method;
                if (!Object.class.equals(candidate.getParameterTypes()[0])
                    || !Object.class.equals(candidate.getParameterTypes()[1])
                ) {
                    break;
                }
            }
        }
        return candidate;
    }
}
