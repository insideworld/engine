package insideworld.engine.integration.tests.some.cglib;

import io.quarkus.test.junit.QuarkusTest;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.Test;

@QuarkusTest
public class TestCgLib {

    private EntityManager manager;

    @Inject
    public TestCgLib(EntityManager manager) {
        this.manager = manager;
    }

    @Test
    public void test() {
        System.out.println(manager);
    }
}
