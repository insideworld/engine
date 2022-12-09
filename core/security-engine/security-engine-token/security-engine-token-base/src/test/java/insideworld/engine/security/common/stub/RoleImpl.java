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

package insideworld.engine.security.common.stub;

import insideworld.engine.data.generator.inmemory.entity.abstracts.MemoryEntity;
import insideworld.engine.security.common.entities.Role;
import javax.enterprise.context.Dependent;

@Dependent
public class RoleImpl implements Role, MemoryEntity {

    private long id;

    private String name;

    private Role append;

    @Override
    public long getId() {
        return this.id;
    }

    @Override
    public void setId(long pid) {
        this.id = pid;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public void setName(final String pname) {
        this.name = pname;
    }

    @Override
    public Role getAppend() {
        return this.append;
    }

    @Override
    public void setAppend(final Role pappend) {
        this.append = pappend;
    }


}
