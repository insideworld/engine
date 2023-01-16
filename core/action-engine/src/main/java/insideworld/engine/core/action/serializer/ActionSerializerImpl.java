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

import insideworld.engine.core.action.executor.ActionsInfo;
import insideworld.engine.core.action.executor.key.Key;
import insideworld.engine.core.common.exception.CommonException;
import insideworld.engine.core.common.startup.OnStartUp;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;
import javax.inject.Singleton;
import org.apache.commons.lang3.tuple.Pair;

@Singleton
public class ActionSerializerImpl implements OnStartUp, ActionSerializer {

    private final List<Serializer> sorted;

    private final Map<Key<?, ?>, Serializer> serializers;

    private final Map<Key<?, ?>, Pair<Class<?>, Serializer>> deserializers;

    private final ActionsInfo info;

    @Inject
    public ActionSerializerImpl(final List<Serializer> sorted, final ActionsInfo info) {
        this.sorted = sorted.stream()
            .sorted(Comparator.comparingLong(Serializer::order).reversed())
            .toList();
        this.serializers = new HashMap<>();
        this.deserializers = new HashMap<>();
        this.info = info;
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
        final Pair<Class<?>, Serializer> pair = this.deserializers.get(key);
        return pair.getRight().deserialize(
            stream,
            pair.getLeft()
        );
    }

    @Override
    public void startUp() throws CommonException {
        for (final Key<?, ?> key : this.info.getKeys()) {
            final Pair<Class<?>, Class<?>> pair = this.info.resolveTypes((Key<Object, Object>) key);
            for (final Serializer serializer : this.sorted) {
                if (serializer.applicable(pair.getLeft())) {
                    this.deserializers.put(key, Pair.of(pair.getLeft(), serializer));
                    break;
                }
            }
            for (final Serializer serializer : this.sorted) {
                if (serializer.applicable(pair.getRight())) {
                    this.serializers.put(key, serializer);
                    break;
                }
            }
        }
    }

    @Override
    public long startOrder() {
        return 200_000;
    }

}
