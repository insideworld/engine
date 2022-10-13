/*
 * Copyright (c) 2022 Anton Eliseev
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package insideworld.engine.integration.entities.convertor;


import insideworld.engine.data.generator.jpa.entity.annotations.GenerateJpaEntity;
import insideworld.engine.data.generator.jpa.storage.annotations.GenerateCrud;
import insideworld.engine.generator.GenerateMixin;
import insideworld.engine.generator.entities.actions.delete.annotations.GenerateDeleteAction;
import insideworld.engine.generator.entities.actions.read.annotations.GenerateReadAction;
import insideworld.engine.generator.entities.actions.write.annotations.GenerateWriteAction;

@GenerateJpaEntity(entity = TestOne.class, schema = "entities", table = "one")
@GenerateCrud(entity = TestOne.class)
@GenerateReadAction(entity = TestOne.class, tag = "one", tags = "ones", key = "one.read")
@GenerateWriteAction(entity = TestOne.class, tag = "one", key = "one.write")
@GenerateDeleteAction(entity = TestOne.class, tag = "one", tags = "ones", key = "one.delete")

@GenerateJpaEntity(entity = TestTwo.class, schema = "entities", table = "two")
@GenerateCrud(entity = TestTwo.class)
@GenerateReadAction(entity = TestTwo.class, tag = "two", tags = "twos", key = "two.read")
@GenerateWriteAction(entity = TestTwo.class, tag = "two", key = "two.write")
@GenerateDeleteAction(entity = TestTwo.class, tag = "two", tags = "twos", key = "two.delete")

@GenerateJpaEntity(entity = TestArray.class, schema = "entities", table = "arrays")
@GenerateCrud(entity = TestArray.class)
@GenerateReadAction(entity = TestArray.class, tag = "array", tags = "arrays", key = "array.read")
@GenerateWriteAction(entity = TestArray.class, tag = "array", key = "array.write")
@GenerateDeleteAction(entity = TestArray.class, tag = "array", tags = "arrays", key = "array.delete")

@GenerateJpaEntity(entity = TestMain.class, schema = "entities", table = "main")
@GenerateCrud(entity = TestMain.class)
@GenerateReadAction(entity = TestMain.class, tag = "main", tags = "mains", key = "main.read")
@GenerateWriteAction(entity = TestMain.class, tag = "main", key = "main.write")
@GenerateDeleteAction(entity = TestMain.class, tag = "main", tags = "mains", key = "main.delete")
public interface Mixin extends GenerateMixin {
}
