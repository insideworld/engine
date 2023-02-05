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

import insideworld.engine.example.quarkus.common.data.SomeData;
import insideworld.engine.example.quarkus.common.roles.MemberRole;
import insideworld.engine.plugins.generator.base.GenerateMixin;
import insideworld.engine.plugins.generator.data.action.delete.annotations.GenerateDeleteAction;
import insideworld.engine.plugins.generator.data.action.read.id.annotations.GenerateReadAction;
import insideworld.engine.plugins.generator.data.action.read.specific.annotations.GenerateSpecificReadAction;
import insideworld.engine.plugins.generator.data.action.write.annotations.GenerateWriteAction;


@GenerateReadAction(entity = PrimaryEntity.class, key = "PrimaryEntity.read", interfaces = {MemberRole.class})
@GenerateSpecificReadAction(
    storage = PrimaryStorage.class,
    inputType = String.class,
    method = "getByValue",
    parameters = {},
    key = "PrimaryEntity.readByValue",
    interfaces = {MemberRole.class}
)
@GenerateWriteAction(entity = PrimaryEntity.class, key = "PrimaryEntity.write", interfaces = {MemberRole.class})
@GenerateDeleteAction(entity = PrimaryEntity.class, key = "PrimaryEntity.delete", interfaces = {MemberRole.class})

@GenerateReadAction(entity = NestedEntity.class, key = "NestedEntity.read", interfaces = {MemberRole.class})
@GenerateWriteAction(entity = NestedEntity.class, key = "NestedEntity.write", interfaces = {MemberRole.class})
@GenerateDeleteAction(entity = NestedEntity.class, key = "NestedEntity.delete", interfaces = {MemberRole.class})

@GenerateReadAction(entity = NestedsEntity.class, key = "NestedsEntity.read", interfaces = {MemberRole.class})
@GenerateWriteAction(entity = NestedsEntity.class, key = "NestedsEntity.write", interfaces = {MemberRole.class})
@GenerateDeleteAction(entity = NestedsEntity.class, key = "NestedsEntity.delete", interfaces = {MemberRole.class})

@GenerateReadAction(entity = SingleEntity.class, key = "SingleEntity.read", interfaces = {MemberRole.class})
@GenerateWriteAction(entity = SingleEntity.class, key = "SingleEntity.write", interfaces = {MemberRole.class})
@GenerateDeleteAction(entity = SingleEntity.class, key = "SingleEntity.delete", interfaces = {MemberRole.class})
public interface MixinActions extends GenerateMixin {
}
