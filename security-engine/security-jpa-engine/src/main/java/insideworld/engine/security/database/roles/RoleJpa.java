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
