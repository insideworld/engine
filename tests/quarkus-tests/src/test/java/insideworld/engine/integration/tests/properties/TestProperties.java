package insideworld.engine.integration.tests.properties;

import insideworld.engine.properties.PropertiesProvider;
import io.quarkus.test.junit.QuarkusTest;
import javax.inject.Inject;
import org.junit.jupiter.api.Test;

@QuarkusTest
public class TestProperties {

    private final PropertiesProvider properties;

    @Inject
    public TestProperties(final PropertiesProvider properties) {
        this.properties = properties;
    }

    @Test
    public void test() {
        final String test = this.properties.provide("test", String.class);
        assert test.equals("value");
    }
}
