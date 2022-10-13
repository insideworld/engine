/*
 * Copyright (c) 2022 Anton Eliseev
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package insideworld.engine.data.generator.inmemory.entity;

import insideworld.engine.data.generator.inmemory.entity.abstracts.AbstractMemoryEntity;
import insideworld.engine.data.generator.inmemory.entity.search.InMemoryInfo;
import io.quarkus.gizmo.ClassCreator;
import io.quarkus.gizmo.ClassOutput;
import javax.enterprise.context.Dependent;

public class InMemoryEntityClassGenerator {

    private final ClassOutput output;

    public InMemoryEntityClassGenerator(final ClassOutput output) {
        this.output = output;
    }

    public ClassCreator createEntity(final InMemoryInfo info) {
        final ClassCreator creator = ClassCreator.builder()
            .classOutput(this.output)
            .className(info.getImplementation())
            .superClass(AbstractMemoryEntity.class)
            .interfaces(info.getEntity())
            .build();
        creator.addAnnotation(Dependent.class);
        return creator;
    }

}
