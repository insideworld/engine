package insideworld.engine.database.schema;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import javax.inject.Singleton;

@Singleton
public class DBQueriesStorage implements QueriesStorage, PanacheRepositoryBase<JpaQueries, String> {

    @Override
    public Queries getQuery(final String name) {
        return findById(name);
    }
}
