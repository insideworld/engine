package insideworld.engine.integration.entities.convertor;

import insideworld.engine.database.AbstractEntity;
import javax.enterprise.context.Dependent;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(schema = "entities", name = "arrays")
@Dependent
public class JpaArray extends AbstractEntity {

    @Column(name = "main_id")
    private long mainId;

    @Column
    private String message;

    public String getMessage() {
        return message;
    }

    public long getMainId() {
        return mainId;
    }

    public void setMainId(long mainId) {
        this.mainId = mainId;
    }
}
