/*
 * Copyright (c) 2022 Anton Eliseev
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package insideworld.engine.data.generator.jpa.entity;

import com.google.common.collect.Maps;
import insideworld.engine.data.generator.jpa.entity.search.JpaInfo;
import insideworld.engine.data.jpa.AbstractEntity;
import insideworld.engine.entities.Entity;
import io.quarkus.gizmo.AnnotationCreator;
import io.quarkus.gizmo.ClassCreator;
import io.quarkus.gizmo.ClassOutput;
import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;
import javax.enterprise.context.Dependent;
import javax.persistence.Table;
import org.apache.commons.lang3.tuple.Pair;

public class EntityClassGenerator {

    private final ClassOutput output;

    public EntityClassGenerator(final ClassOutput output) {
        this.output = output;
    }

    public ClassCreator createEntity(final JpaInfo info) {
        final ClassCreator creator = ClassCreator.builder()
            .classOutput(this.output)
            .className(info.getImplementation())
            .superClass(AbstractEntity.class)
            .interfaces(info.getEntity())
            .build();
        creator.addAnnotation(Dependent.class);
        creator.addAnnotation(javax.persistence.Entity.class);
        AnnotationCreator annotationCreator = creator.addAnnotation(Table.class);
        annotationCreator.addValue("name", info.getTable());
        annotationCreator.addValue("schema", info.getSchema());
        return creator;
    }

}
