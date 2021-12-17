package insideworld.engine.quarkus;

import com.google.common.collect.ImmutableList;
import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;
import javax.enterprise.inject.Instance;

public abstract class AbstractBeans {

    protected <T> Collection<T> fromInstance(final Instance<T> instance) {
        return instance.stream().collect(Collectors.toUnmodifiableList());
    }
}
