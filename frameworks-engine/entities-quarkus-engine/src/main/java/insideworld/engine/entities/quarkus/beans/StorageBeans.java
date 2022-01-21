package insideworld.engine.entities.quarkus.beans;

import insideworld.engine.entities.converter.dto.mapper.Mapper;
import insideworld.engine.entities.storages.Storage;
import insideworld.engine.quarkus.AbstractBeans;
import io.quarkus.arc.All;
import io.quarkus.arc.DefaultBean;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import javax.enterprise.inject.Default;
import javax.enterprise.inject.Instance;
import javax.enterprise.inject.Produces;

public class StorageBeans extends AbstractBeans {

    /**
     * Stupid CDI can't work properly with wildcards
     * @param instance
     * @return
     */
    @Produces
    public Collection<Storage> storages(final Instance<Storage<?>> instance) {
        return instance.stream().map(bean -> (Storage) bean)
            .collect(Collectors.toUnmodifiableList());
    }

    @Produces
    public Collection<Mapper> mappers(final Instance<Mapper> instance) {
        return this.fromInstance(instance);
    }
}
