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

package insideworld.engine.integration.entities.generated;


import insideworld.engine.data.generator.inmemory.entity.annotations.GenerateInMemoryEntity;
import insideworld.engine.data.generator.inmemory.storage.annotations.GenerateInMemoryCrud;
import insideworld.engine.data.generator.jpa.entity.annotations.GenerateJpaEntity;
import insideworld.engine.data.generator.jpa.storage.annotations.GenerateCrud;
import insideworld.engine.generator.GenerateMixin;
import insideworld.engine.generator.entities.actions.delete.annotations.GenerateDeleteAction;
import insideworld.engine.generator.entities.actions.read.annotations.GenerateReadAction;
import insideworld.engine.generator.entities.actions.write.annotations.GenerateWriteAction;


@GenerateJpaEntity(entity = GeneratedChildEntity.class, schema = "generated", table = "child")
@GenerateJpaEntity(entity = GeneratedTopEntity.class, schema = "generated", table = "top")
@GenerateCrud(entity = GeneratedChildEntity.class)
@GenerateCrud(entity = GeneratedTopEntity.class)
@GenerateReadAction(entity = GeneratedChildEntity.class, tag = "child", tags = "childs", key = "child.read")
@GenerateReadAction(entity = GeneratedTopEntity.class, tag = "top", tags = "tops", key = "top.read")
@GenerateWriteAction(entity = GeneratedChildEntity.class, tag = "child", key = "child.write")
@GenerateWriteAction(entity = GeneratedTopEntity.class, tag = "top", key = "top.write")
@GenerateDeleteAction(entity = GeneratedChildEntity.class, tag = "child", tags = "childs", key = "child.delete")
@GenerateDeleteAction(entity = GeneratedTopEntity.class, tag = "top", tags = "tops", key = "top.delete")

@GenerateInMemoryEntity(entity = GeneratedInMemoryEntity.class)
@GenerateInMemoryCrud(entity = GeneratedInMemoryEntity.class)

public interface Mixin extends GenerateMixin {
}
