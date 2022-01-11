package insideworld.engine.data.generator.jpa.entity.search;

import insideworld.engine.entities.Entity;
import java.util.Collection;
import java.util.Map;
import javax.persistence.Table;

public interface SearchEntities {

    Map<Class<? extends Entity>, TableInfo> search();

}
