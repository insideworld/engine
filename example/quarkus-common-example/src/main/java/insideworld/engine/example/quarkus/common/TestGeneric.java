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

package insideworld.engine.example.quarkus.common;

import insideworld.engine.core.action.Action;
import insideworld.engine.core.common.exception.CommonException;
import insideworld.engine.example.quarkus.common.newdata.PrimaryEntity;
import java.util.Collection;
import javax.enterprise.util.TypeLiteral;

public class TestGeneric implements Action<Collection<Long>, Collection<PrimaryEntity>> {
    @Override
    public Collection<PrimaryEntity> execute(final Collection<Long> input) throws CommonException {
        return null;
    }

    @Override
    public String key() {
        return null;
    }

    @Override
    public Class<? extends Collection<Long>> inputType() {
        final TypeLiteral<Collection<Long>> typeLiteral = new TypeLiteral<>() {};
        return typeLiteral.getRawType();
    }

    @Override
    public Class<? extends Collection<PrimaryEntity>> outputType() {
        return null;
    }
}
