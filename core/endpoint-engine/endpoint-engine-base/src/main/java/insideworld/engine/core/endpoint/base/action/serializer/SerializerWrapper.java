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
import insideworld.engine.core.action.executor.ExecuteContext;
import insideworld.engine.core.action.executor.ExecutorTags;
import insideworld.engine.core.action.executor.profile.ExecuteProfile;
import insideworld.engine.core.action.executor.profile.wrapper.AbstractExecuteWrapper;
import insideworld.engine.core.common.exception.CommonException;
import insideworld.engine.core.common.startup.OnStartUp;
import insideworld.engine.core.endpoint.base.action.EndpointProfile;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class SerializerWrapper extends AbstractExecuteWrapper implements OnStartUp {

    private final List<Serializer> serializers;

    private final Map<Class<Action<?,?>>, Serializer> map;
    private final List<Action<?, ?>> actions;

    @Inject
    public SerializerWrapper(
        final List<Serializer> serializers,
        final List<Action<?,?>> actions
    ) {
        this.serializers = serializers;
        this.map = new HashMap<>(serializers.size());
        this.actions = actions;
    }

    public void execute(final ExecuteContext context) throws CommonException {
        final Action<?, ?> action = context.get(ExecutorTags.ACTION);
        final Serializer serializer = this.map.get(action.getClass());
        serializer.serialize(
            context.get(ExecutorTags.OUTPUT),
            action.outputType(),
            context.get(EndpointTags.OUTPUT)
        );
        super.execute(context);
    }

    @Override
    public long wrapperOrder() {
        return -900_000;
    }

    @Override
    public Collection<Class<? extends ExecuteProfile>> forProfile() {
        return Collections.singleton(EndpointProfile.class);
    }

    @Override
    public void startUp() throws CommonException {
        final List<Serializer> sorted = this.serializers.stream()
            .sorted(Comparator.comparingLong(Serializer::order).reversed())
            .toList();
        for (final Action<?, ?> action : this.actions) {
            for (final Serializer serializer : sorted) {
                if (serializer.applicable(action.outputType())) {
                    this.map.put((Class<Action<?, ?>>) action.getClass(), serializer);
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
