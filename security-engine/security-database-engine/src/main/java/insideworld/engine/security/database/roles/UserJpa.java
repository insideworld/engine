package insideworld.engine.security.database.roles;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.collect.Sets;
import insideworld.engine.database.AbstractEntity;
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
