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

package insideworld.engine.example.quarkus.common.newdata;

import insideworld.engine.plugins.generator.base.GenerateMixin;
import insideworld.engine.plugins.generator.data.jpa.entity.annotations.GenerateJpaEntity;
import insideworld.engine.plugins.generator.data.jpa.storage.annotations.GenerateCrud;

@GenerateJpaEntity(
    entity = PrimaryEntity.class,
    schema = "test",
    table = "primary"
)
@GenerateJpaEntity(
    entity = SingleEntity.class,
    schema = "test",
    table = "single",
    oneToOne = "primary"
)
@GenerateJpaEntity(
    entity = NestedEntity.class,
    schema = "test",
    table = "nested"
)
@GenerateJpaEntity(
    entity = NestedsEntity.class,
    schema = "test",
    table = "nesteds"
)
@GenerateCrud(entity = PrimaryEntity.class)
@GenerateCrud(entity = SingleEntity.class)
@GenerateCrud(entity = NestedEntity.class)
@GenerateCrud(entity = NestedsEntity.class)
public interface MixinJpa extends GenerateMixin {
}
