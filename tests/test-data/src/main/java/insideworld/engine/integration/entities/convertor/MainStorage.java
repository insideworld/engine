package insideworld.engine.integration.entities.convertor;

import insideworld.engine.data.jpa.AbstractCrudGenericStorage;
import insideworld.engine.entities.storages.Storage;
import insideworld.engine.entities.storages.StorageException;
import io.quarkus.hibernate.orm.panache.Panache;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import java.util.Collection;
import java.util.stream.Collectors;
import javax.inject.Singleton;
import javax.persistence.EntityManager;

//@Singleton
//public class MainStorage extends AbstractCrudGenericStorage<JpaTestMain, JpaTestMain> {
//
//}

//

@Singleton
public class MainStorage extends AbstractCrudGenericStorage<TestMain, JpaTestMain> {


}