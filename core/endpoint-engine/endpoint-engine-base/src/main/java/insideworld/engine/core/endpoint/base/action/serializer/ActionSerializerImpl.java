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

import insideworld.engine.core.action.executor.key.Key;
import insideworld.engine.core.common.exception.CommonException;
import insideworld.engine.core.common.startup.OnStartUp;
import insideworld.engine.core.endpoint.base.serializer.Serializer;
import insideworld.engine.core.endpoint.base.serializer.SerializerFacade;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class ActionSerializerImpl implements OnStartUp, ActionSerializer {

    private final Map<Key<?, ?>, Serializer> serializers;

    private final Map<Key<?, ?>, Serializer> deserializers;
    private final ActionTypes types;
    private final SerializerFacade facade;

    @Inject
    public ActionSerializerImpl(final ActionTypes types, final SerializerFacade facade) {
        this.types = types;
        this.facade = facade;
        this.serializers = new HashMap<>();
        this.deserializers = new HashMap<>();
    }

    @Override
    public <T> void serialize(final Key<?, T> key, final T value, final OutputStream stream)
        throws CommonException {
        final Serializer serializer = this.serializers.get(key);
        serializer.serialize(value, stream);
    }

    @Override
    public <T> T deserialize(final Key<T, ?> key, final InputStream stream)
        throws CommonException {
        final Serializer serializer = this.deserializers.get(key);
        return (T) serializer.deserialize(stream);
    }

    @Override
    public void startUp() throws CommonException {
        for (final var entry : this.types.getInputOutputs().entrySet()) {
            this.deserializers.put(
                entry.getKey(), this.facade.getSerializer(entry.getValue().getLeft())
            );
            this.serializers.put(
                entry.getKey(), this.facade.getSerializer(entry.getValue().getRight())
            );
        }
    }

    @Override
    public long startOrder() {
        return 200_000;
    }

}
