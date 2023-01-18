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

package insideworld.engine.core.security.jpa.data;

import com.google.common.collect.Sets;
import insideworld.engine.core.data.jpa.AbstractJpaIdEntity;
import insideworld.engine.core.security.core.data.Role;
import insideworld.engine.core.security.core.data.User;
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
@Table(schema = "security", name = "users")
@Dependent
@Cacheable
public class UserJpa extends AbstractJpaIdEntity implements User {

    @ManyToOne()
    @JoinColumn(name = "role")
    @org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private RoleJpa role;

    @Column(name = "name")
    private String name;

    @Transient
    private Collection<Role> roles;

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
        for (final Role child : role.getChildren()) {
            roles.addAll(fetchRole(child));
        }
        return roles;
    }
}
