///*
// * Copyright (c) 2022 Anton Eliseev
// *
// * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
// * associated documentation files (the "Software"), to deal in the Software without restriction,
// * including without limitation the rights to use, copy, modify, merge, publish, distribute,
// * sublicense, and/or sell copies of the Software, and to permit persons to whom the Software
// * is furnished to do so, subject to the following conditions:
// *
// * The above copyright notice and this permission notice shall be included in all copies or
// * substantial portions of the Software.
// *
// * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT
// * NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
// * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
// * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
// * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
// */
//
//package insideworld.engine.security.common.stubs.data;
//
//import com.google.common.collect.Lists;
//import insideworld.engine.plugins.generator.data.inmemory.entity.abstracts.MemoryEntity;
//import insideworld.engine.core.security.core.data.Role;
//import java.util.Collection;
//import javax.enterprise.context.Dependent;
//
//
//@Dependent
//public class RoleImpl implements Role, MemoryEntity {
//
//    private long id;
//
//    private String name;
//
//    private final Collection<Role> children = Lists.newLinkedList();
//
//    @Override
//    public long getId() {
//        return this.id;
//    }
//
//    @Override
//    public void setId(long pid) {
//        this.id = pid;
//    }
//
//    @Override
//    public String getName() {
//        return this.name;
//    }
//
//    @Override
//    public void setName(final String pname) {
//        this.name = pname;
//    }
//
//    @Override
//    public Collection<Role> getChildren() {
//        return this.children;
//    }
//
//    @Override
//    public void addChildren(final Role child) {
//        this.children.add(child);
//    }
//
//    @Override
//    public final int hashCode() {
//        return this.name.hashCode();
//    }
//
//    @Override
//    public final boolean equals(final Object obj) {
//        final boolean result;
//        if (obj instanceof Role) {
//            result = this.name.equals(((Role) obj).getName());
//        } else {
//            result = false;
//        }
//        return result;
//    }
//
//}
