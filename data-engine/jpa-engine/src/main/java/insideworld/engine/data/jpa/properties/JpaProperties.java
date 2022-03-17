package insideworld.engine.data.jpa.properties;

import javax.enterprise.context.Dependent;
import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(schema = "engine", name = "properties")
@Dependent
@Cacheable
public class JpaProperties {

    @Id
    @Column(name = "key", unique = true, nullable = false)
    private String key;

    @Column(name = "value")
    private String value;

    public String getKey() {
        return this.key;
    }

    public void setKey(final String key) {
        this.key = key;
    }

    public String getValue() {
        return this.value;
    }

    public void setValue(final String value) {
        this.value = value;
    }
}
