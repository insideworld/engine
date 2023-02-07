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

package insideworld.engine.core.endpoint.base.action.serializer;

import insideworld.engine.core.action.Action;
import insideworld.engine.core.action.executor.ActionExecutor;
import insideworld.engine.core.action.executor.key.Key;
import insideworld.engine.core.common.exception.CommonException;
import insideworld.engine.core.common.startup.OnStartUp;
import insideworld.engine.core.endpoint.base.serializer.Serializer;
import insideworld.engine.core.endpoint.base.serializer.SerializerFacade;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class ActionSerializerImpl implements ActionSerializer {

    private final Map<Key<?, ?>, Serializer> serializers;

    private final Map<Key<?, ?>, Serializer> deserializers;
    private final SerializerFacade facade;
    private final ActionExecutor executor;

    @Inject
    public ActionSerializerImpl(final SerializerFacade facade, final ActionExecutor executor) {
        this.facade = facade;
        this.executor = executor;
        this.serializers = new ConcurrentHashMap<>();
        this.deserializers = new ConcurrentHashMap<>();
    }

    @Override
    public <T> void serialize(final Key<?, T> key, final T value, final OutputStream stream)
        throws CommonException {
        Serializer serializer = this.serializers.get(key);
        if (serializer == null) {
           serializer = this.createSerializer(key, this.serializers);
        }
        serializer.serialize(value, stream);
    }

    @Override
    public <T> T deserialize(final Key<T, ?> key, final InputStream stream)
        throws CommonException {
        Serializer serializer = this.deserializers.get(key);
        if (serializer == null) {
            serializer = this.createSerializer(key, this.deserializers);
        }
        return (T) serializer.deserialize(stream);
    }

    private synchronized <I, O> Serializer createSerializer(
        final Key<I, O> key,
        final Map<Key<?, ?>, Serializer> map
    ) {
        Serializer serializer = map.get(key);
        if (serializer == null) {
            final Action<?, ?> action = this.executor.getKeys().get(key);
            final Method method = this.findMethod(action);
            this.deserializers.put(key, this.facade.getSerializer(new MethodType(method, 0)));
            this.serializers.put(key, this.facade.getSerializer(new MethodType(method, 1)));
            serializer = map.get(key);
        }
        return serializer;
    }


    /**
     * Find a valid method.
     * Because inheritance from interface create a method with Object, Object arguments -
     * need to find first where one of argument not equals to Object.
     *
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
