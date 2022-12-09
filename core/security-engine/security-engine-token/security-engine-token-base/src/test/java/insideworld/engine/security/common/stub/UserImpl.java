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

import com.google.common.collect.Sets;
import insideworld.engine.data.generator.inmemory.entity.abstracts.MemoryEntity;
import insideworld.engine.security.common.entities.Role;
import insideworld.engine.security.common.entities.User;
import java.util.Collection;
import javax.enterprise.context.Dependent;

@Dependent
public class UserImpl implements MemoryEntity, User {

    private long id;

    private String token;

    private Role role;

    private String name;

    @Override
    public long getId() {
        return this.id;
    }

    @Override
    public void setId(long pid) {
        this.id = pid;
    }

    @Override
    public String getToken() {
        return this.token;
    }

    @Override
    public void setToken(final String ptoken) {
        this.token = ptoken;
    }

    @Override
    public Role getRole() {
        return this.role;
    }

    @Override
    public void setRole(final Role role) {
        this.role = role;
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
    public Collection<Role> getAvailableRoles() {
        return UserImpl.fetchRole(this.role);
    }

    private static Collection<Role> fetchRole(final Role role) {
        final Collection<Role> roles = Sets.newHashSet();
        roles.add(role);
        if (role.getAppend() != null) {
            roles.addAll(UserImpl.fetchRole(role.getAppend()));
        }
        return roles;
    }

}
