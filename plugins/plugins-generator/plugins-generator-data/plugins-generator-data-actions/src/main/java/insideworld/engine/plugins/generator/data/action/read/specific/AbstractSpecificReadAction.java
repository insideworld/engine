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

package insideworld.engine.plugins.generator.data.action.read.specific;

import insideworld.engine.core.action.Action;
import insideworld.engine.core.common.exception.CommonException;
import insideworld.engine.core.data.core.Entity;
import insideworld.engine.core.data.core.storages.Storage;

/**
 * This class is abstract.
 * Add generics in signature.
 * Generate:
 * 1. Key method.
 * 2. InputType method
 * 3. OutputType method (based on returned type)
 * 4. Generate execute with parsing fields.
 * @param <I>
 * @param <O>
 * @param <S>
 */
public abstract class AbstractSpecificReadAction<I, O, S extends Storage<? extends Entity>> implements Action<I, O> {


    protected final S storage;

    public AbstractSpecificReadAction(final S storage) {
        this.storage = storage;
    }

//    @Override
//    public O execute(final I input) throws CommonException {
//        return this.storage.readSome(input.getOne(), input.getTwo());
//        return null;
//    }
//
//    @Override
//    public String key() {
//        return provided key;
//    }
//
//    @Override
//    public Class<? extends I> inputType() {
//        return I.class;
//    }
//
//    @Override
//    public Class<? extends O> outputType() {
//        return O.class;
//    }
}

//    @Override
//    public void execute(final Context context, final Output output) throws CommonException {
//        for (final Entity t : this.read(context)) {
//            output.add(this.converter.convert(t));
//        }
//    }
//    protected abstract Collection<? extends Entity> read(Context context);

//}
