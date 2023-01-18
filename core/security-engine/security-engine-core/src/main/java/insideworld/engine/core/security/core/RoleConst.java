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

package insideworld.engine.core.security.core;


import insideworld.engine.core.security.core.data.Role;
import java.util.Collection;

/**
 * Role implementation to hardcode into the code.
 * @since 0.6.0
 */
public class RoleConst implements Role {

    private final String name;

    public RoleConst(final String name) {
        this.name = name;
    }

    @Override
    public final long getId() {
        return 0;
    }


    @Override
    public final String getName() {
        return this.name;
    }

    @Override
    public final void setName(final String name) {
        throw new UnsupportedOperationException("Read only object.");
    }

    @Override
    public Collection<Role> getChildren() {
        throw new UnsupportedOperationException("For constant roles this not available.");
    }

    @Override
    public void addChildren(Role child) {
        throw new UnsupportedOperationException("For constant roles this not available.");
    }

    @Override
    public final int hashCode() {
        return this.name.hashCode();
    }

    @Override
    public final boolean equals(final Object obj) {
        final boolean result;
        if (obj instanceof Role) {
            result = this.name.equals(((Role) obj).getName());
        } else {
            result = false;
        }
        return result;
    }
}
