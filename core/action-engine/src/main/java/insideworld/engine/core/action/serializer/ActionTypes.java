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

import com.google.common.collect.Sets;
import insideworld.engine.core.action.Action;
import insideworld.engine.core.common.injection.ObjectFactory;
import java.util.List;
import java.util.Set;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class ActionTypes implements Types {

    private final Set<Class<?>> inputs;

    private final Set<Class<?>> outputs;
    private final ObjectFactory factory;

    @Inject
    public ActionTypes(final List<Action<?,?>> actions, final ObjectFactory factory) {
        this.inputs = Sets.newHashSetWithExpectedSize(actions.size());
        this.outputs = Sets.newHashSetWithExpectedSize(actions.size());
        this.factory = factory;
        for (final Action<?, ?> action : actions) {
            final Class<?> input = action.inputType();
            this.inputs.add(input);
            if (input.isInterface()) {
                this.inputs.add(this.factory.implementation(input));
            }
            final Class<?> output = action.outputType();
            this.outputs.add(output);
            if (output.isInterface()) {
                this.outputs.add(this.factory.implementation(output));
            }
        }
        this.outputs.remove(null);
        this.inputs.remove(null);
    }

    @Override
    public Set<Class<?>> getInputs() {
        return this.inputs;
    }

    @Override
    public Set<Class<?>> getOutputs() {
        return this.outputs;
    }
}
