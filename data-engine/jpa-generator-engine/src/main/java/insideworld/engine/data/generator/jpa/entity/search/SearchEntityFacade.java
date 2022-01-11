package insideworld.engine.data.generator.jpa.entity.search;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import insideworld.engine.entities.Entity;
import insideworld.engine.reflection.Reflection;
import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

public class SearchEntityFacade implements SearchEntities {

    private final Collection<SearchEntities> searchers;

    public SearchEntityFacade(final Reflection reflection) {
        this.searchers = ImmutableList.of(
            new SearchMixin(reflection)
        );
    }

    @Override
    public Map<Class<? extends Entity>, TableInfo> search() {
        return this.searchers.stream()
            .map(SearchEntities::search)
            .flatMap(search -> search.entrySet().stream())
            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }
}
