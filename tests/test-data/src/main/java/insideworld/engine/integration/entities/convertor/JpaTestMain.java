package insideworld.engine.integration.entities.convertor;

import insideworld.engine.data.jpa.AbstractEntity;
import java.util.Collection;
import javax.enterprise.context.Dependent;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(schema = "entities", name = "main")
@Dependent
public class JpaTestMain extends AbstractEntity {

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @ManyToOne()
    @JoinColumn(name = "one")
    private JpaTestOne one;

    @ManyToOne()
    @JoinColumn(name = "two")
    private JpaTestTwo two;

    @OneToMany(mappedBy = "mainId")
    private Collection<JpaArray> arrays;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public JpaTestOne getOne() {
        return one;
    }

    public void setOne(JpaTestOne one) {
        this.one = one;
    }

    public JpaTestTwo getTwo() {
        return two;
    }

    public void setTwo(JpaTestTwo two) {
        this.two = two;
    }

    public Collection<JpaArray> getArrays() {
        return arrays;
    }

    public void setArrays(Collection<JpaArray> arrays) {
        this.arrays = arrays;
    }
}
