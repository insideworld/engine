package insideworld.engine.quarkus.beans;

import insideworld.engine.properties.Properties;
import insideworld.engine.quarkus.AbstractBeans;
import java.util.Collection;
import javax.enterprise.inject.Instance;
import javax.enterprise.inject.Produces;

public class PropertiesBeans extends AbstractBeans {

    @Produces
    public Collection<Properties> properties(final Instance<Properties> instance) {
        return this.fromInstance(instance);
    }

}
