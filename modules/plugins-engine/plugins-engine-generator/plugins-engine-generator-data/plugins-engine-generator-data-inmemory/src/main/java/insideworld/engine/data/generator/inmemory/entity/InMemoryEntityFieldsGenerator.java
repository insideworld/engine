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

package insideworld.engine.data.generator.inmemory.entity;

import com.google.common.collect.ImmutableList;
import insideworld.engine.data.generator.AbstractEntityFieldsGenerator;
import insideworld.engine.data.generator.FieldGenerator;
import insideworld.engine.data.generator.inmemory.entity.fields.EntitiesFieldGenerator;
import insideworld.engine.data.generator.inmemory.entity.fields.EntityFieldGenerator;
import insideworld.engine.data.generator.inmemory.entity.fields.PrimitiveFieldGenerator;
import insideworld.engine.data.generator.inmemory.entity.search.InMemoryInfo;
import java.util.Collection;

public class InMemoryEntityFieldsGenerator extends AbstractEntityFieldsGenerator<InMemoryInfo> {

    @Override
    protected Collection<FieldGenerator<InMemoryInfo>> createGenerators() {
        return ImmutableList.of(
            new EntitiesFieldGenerator(),
            new EntityFieldGenerator(),
            new PrimitiveFieldGenerator()
        );
    }
}
