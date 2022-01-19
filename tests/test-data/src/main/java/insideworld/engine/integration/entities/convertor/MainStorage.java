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
public class MainStorage implements Storage<TestMain>, PanacheRepository<TestMain> {

    @Override
    public Collection<TestMain> readAll() {
        return this.castUpper(findAll().list());
    }

    @Override
    public JpaTestMain read(final long id) throws StorageException {
        final EntityManager entityManager = Panache.getEntityManager(JpaTestMain.class);
        final JpaTestMain jpaTestMain = entityManager.find(JpaTestMain.class, id);
        return jpaTestMain;
    }

    @Override
    public Collection<TestMain> read(final Collection<Long> ids) {

        return this.castUpper(find("id in (?1)", ids).list());
    }

    @Override
    public TestMain write(final TestMain entity) {
        persist(this.forCrud().cast(entity));
        return entity;
    }

    @Override
    public Collection<TestMain> writeAll(final Collection<TestMain> entity) {
        persist(this.castLower(entity));
        return entity;
    }

    @Override
    public void delete(final Collection<TestMain> entities) {
        for (final TestMain entity : this.castLower(entities)) {
            delete(entity);
        }
    }

    @Override
    public boolean exists(long id) {
        return findByIdOptional(id).isPresent();
    }

    @Override
    public Class<? extends TestMain> forEntity() {
        return TestMain.class;
    }

    protected Class<TestMain> forCrud() {
        return TestMain.class;
    }

    protected Collection<TestMain> castLower(final Collection<TestMain> collection) {
        final Collection<TestMain> result;
        if (collection == null) {
            result = null;
        } else {
            result = collection.stream()
                .map(item -> this.forCrud().cast(item))
                .collect(Collectors.toList());
        }
        return result;
    }

    protected Collection<TestMain> castUpper(final Collection<TestMain> collection) {
        return collection.stream()
            .map(item -> this.forEntity().cast(item))
            .collect(Collectors.toList());
    }

}