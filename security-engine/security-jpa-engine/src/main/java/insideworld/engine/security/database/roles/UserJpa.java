/*
 * Copyright (c) 2022 Anton Eliseev
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package insideworld.engine.security.database.roles;

import com.google.common.collect.Sets;
import insideworld.engine.data.jpa.AbstractEntity;
import insideworld.engine.security.common.entities.Role;
import insideworld.engine.security.common.entities.User;
import java.util.Collection;
import javax.enterprise.context.Dependent;
import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PostLoad;
import javax.persistence.Table;
import javax.persistence.Transient;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity
@Table(schema = "users", name = "users")
@Dependent
@Cacheable
public class UserJpa extends AbstractEntity implements User {

    @Column(name = "token", unique = true, nullable = false)
    private String token;

    @ManyToOne()
    @JoinColumn(name = "role")
    @org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private RoleJpa role;

    @Column(name = "name")
    private String name;

    @Transient
    private Collection<Role> roles;

    @Override
    public String getToken() {
        return this.token;
    }

    @Override
    public void setToken(final String token) {
        this.token = token;
    }

    @Override
    public Role getRole() {
        return this.role;
    }

    @Override
    public void setRole(final Role role) {
        this.role = (RoleJpa) role;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public void setName(final String name) {
        this.name = name;
    }

    @Override
    public Collection<Role> getAvailableRoles() {
        return this.roles;
    }

    @PostLoad
    public void postLoad() {
        this.roles = this.fetchRole(this.role);
    }

    private Collection<Role> fetchRole(final Role role) {
        final Collection<Role> roles = Sets.newHashSet();
        roles.add(role);
        if (role.getAppend() != null) {
            roles.addAll(this.fetchRole(role.getAppend()));
        }
        return roles;
    }
}
