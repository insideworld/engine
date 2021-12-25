package insideworld.engine.integration.tests.some.cglib;

import insideworld.engine.database.AbstractEntity;
import insideworld.engine.quarkus.extension.MyEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(schema = "entities", name = "cglib")
public class CGEntityImpl extends AbstractEntity implements MyEntity {

    @Column(name = "message")
    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
