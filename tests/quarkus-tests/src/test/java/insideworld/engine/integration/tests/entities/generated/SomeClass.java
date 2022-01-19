package insideworld.engine.integration.tests.entities.generated;

import insideworld.engine.integration.entities.convertor.TestMain;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class SomeClass {

    private final PanacheRepository<TestMain> qwe;

    @Inject
    public SomeClass(PanacheRepository<TestMain> qwe) {
        this.qwe = qwe;
    }

}
