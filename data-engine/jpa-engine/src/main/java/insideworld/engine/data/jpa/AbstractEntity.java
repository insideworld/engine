package insideworld.engine.data.jpa;


import insideworld.engine.entities.Entity;
import javax.management.relation.Role;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class AbstractEntity implements Entity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false, updatable = false)
    private long id;

    public long getId() {
        return this.id;
    }

    @Override
    public int hashCode() {
        return Long.hashCode(this.id);
    }

    @Override
    public boolean equals(final Object obj) {
        return
            obj != null &&
            this.getClass().equals(obj.getClass()) &&
            this.id == ((Entity) obj).getId();
    }

}
