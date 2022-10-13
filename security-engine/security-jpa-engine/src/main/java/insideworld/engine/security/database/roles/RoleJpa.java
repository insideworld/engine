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

import com.fasterxml.jackson.annotation.JsonIgnore;
import insideworld.engine.data.jpa.AbstractEntity;
import insideworld.engine.security.common.entities.Role;
import javax.enterprise.context.Dependent;
import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity
@Table(schema = "users", name = "roles")
@Dependent
@Cacheable
public class RoleJpa extends AbstractEntity implements Role {

    @Column(name = "name")
    private String name;

    @ManyToOne()
    @JoinColumn(name = "append_id")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private RoleJpa append;

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public Role getAppend() {
        return this.append;
    }

    @Override
    public int hashCode() {
        return this.name.hashCode();
    }

    @Override
    public boolean equals(final Object obj) {
        final boolean result;
        if (obj instanceof Role) {
            result = this.name.equals(((Role) obj).getName());
        } else {
            result = false;
        }
        return result;
    }

}
