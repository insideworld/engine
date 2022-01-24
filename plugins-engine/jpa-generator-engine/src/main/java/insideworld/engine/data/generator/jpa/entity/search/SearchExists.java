package insideworld.engine.data.generator.jpa.entity.search;

import com.google.common.collect.Maps;
import insideworld.engine.entities.Entity;
import insideworld.engine.reflection.Reflection;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.persistence.Table;

public class SearchExists implements SearchEntities {

    private Reflection reflection;

    public SearchExists(final Reflection reflection) {
        this.reflection = reflection;
    }

    @Override
    public Collection<JpaInfo> search() {
        return this.reflection.getSubTypesOf(Entity.class)
            .stream()
            .filter(Class::isInterface)
            .map(this::createInfo)
            .filter(Objects::nonNull)
            .collect(Collectors.toList());
    }

    private JpaInfo createInfo(final Class<? extends Entity> entity) {
        var implementation = this.reflection.getSubTypesOf(entity).stream()
            .filter(impl -> impl.isAnnotationPresent(javax.persistence.Entity.class))
            .findAny();
        final JpaInfo info;
        if (implementation.isPresent()) {
            final Table table = implementation.get().getAnnotation(Table.class);
            info = new JpaInfo(
                entity,
                table.schema(),
                table.name(),
                implementation.get().getName(),
                false
            );
        } else {
            info = null;
        }
        return info;
    }
}
