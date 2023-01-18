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

import insideworld.engine.example.quarkus.common.data.InputData;
import insideworld.engine.example.quarkus.common.data.SomeDataStorage;
import insideworld.engine.plugins.generator.data.action.read.specific.annotations.GenerateSpecificReadAction;
import insideworld.engine.plugins.generator.data.jpa.entity.annotations.GenerateJpaEntity;
import insideworld.engine.plugins.generator.data.jpa.storage.annotations.GenerateCrud;
import insideworld.engine.example.quarkus.common.data.SomeData;
import insideworld.engine.example.quarkus.common.roles.MemberRole;
import insideworld.engine.plugins.generator.base.GenerateMixin;
import insideworld.engine.plugins.generator.data.action.delete.annotations.GenerateDeleteAction;
import insideworld.engine.plugins.generator.data.action.read.id.annotations.GenerateReadAction;
import insideworld.engine.plugins.generator.data.action.write.annotations.GenerateWriteAction;

/**
 * Generate JPA entity, storage and actions for SomeData.
 * @since 1.0.0
 */
@GenerateJpaEntity(entity = SomeData.class, schema = "test", table = "some_data")
@GenerateCrud(entity = SomeData.class, override = true)
@GenerateReadAction(entity = SomeData.class, key = "somedata.read", interfaces = {MemberRole.class})
@GenerateWriteAction(entity = SomeData.class, key = "somedata.write", interfaces = {MemberRole.class})
@GenerateDeleteAction(entity = SomeData.class, key = "somedata.delete", interfaces = {MemberRole.class})

@GenerateSpecificReadAction(
    storage = SomeDataStorage.class,
    method = "readByValue",
    key = "somedata.value",
    interfaces = {MemberRole.class},
    inputType = String.class,
    parameters = {}
)

@GenerateSpecificReadAction(
    storage = SomeDataStorage.class,
    method = "readByValueSingle",
    key = "somedata.valueSingle",
    interfaces = {MemberRole.class},
    inputType = String.class,
    parameters = {}
)

@GenerateSpecificReadAction(
    storage = SomeDataStorage.class,
    method = "readByDateAndValue",
    key = "somedata.valueDate",
    interfaces = {MemberRole.class},
    inputType = InputData.class,
    parameters = {"value", "date"}
)
public interface Mixin extends GenerateMixin {
}
