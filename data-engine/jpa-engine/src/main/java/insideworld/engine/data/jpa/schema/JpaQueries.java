package insideworld.engine.data.jpa.schema;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import javax.enterprise.context.Dependent;
import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(schema = "engine", name = "sql_queries")
@Dependent
@Cacheable
public class JpaQueries implements Queries {

    @Id
    @Column(name = "id", unique = true, nullable = false)
    public String name;

    @Column(name = "query")
    public String query;

    @Column(name = "input")
    public String input;

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String getQuery() {
        return this.query;
    }

    @Override
    public List<String> getInput() {
        final List<String> result;
        if (this.input == null) {
            result = Collections.emptyList();
        } else {
            result = Arrays.asList(this.input.split(","));
        }
        return result;
    }
}
