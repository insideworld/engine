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
public class JpaTestMain extends AbstractEntity implements TestMain {

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

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public JpaTestOne getOne() {
        return one;
    }

    @Override
    public void setOne(JpaTestOne one) {
        this.one = one;
    }

    @Override
    public JpaTestTwo getTwo() {
        return two;
    }

    @Override
    public void setTwo(JpaTestTwo two) {
        this.two = two;
    }

    @Override
    public Collection<JpaArray> getArrays() {
        return arrays;
    }

    @Override
    public void setArrays(Collection<JpaArray> arrays) {
        this.arrays = arrays;
    }
}
