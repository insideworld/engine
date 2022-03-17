package insideworld.engine.data.jpa.properties;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import javax.inject.Singleton;

@Singleton
public class PropertyStorage implements PanacheRepositoryBase<JpaProperties, String> {
}
