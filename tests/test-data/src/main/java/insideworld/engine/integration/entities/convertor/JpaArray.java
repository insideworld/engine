package insideworld.engine.integration.entities.convertor;

import insideworld.engine.data.jpa.AbstractEntity;
import javax.enterprise.context.Dependent;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(schema = "entities", name = "arrays")
@Dependent
public class JpaArray extends AbstractEntity implements insideworld.engine.integration.entities.JpaArray {

    @Column(name = "main_id")
    private long mainId;

    @Column
    private String message;

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public long getMainId() {
        return mainId;
    }

    @Override
    public void setMainId(long mainId) {
        this.mainId = mainId;
    }
}
