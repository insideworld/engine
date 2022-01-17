package insideworld.engine.data.generator.jpa.storage.search;

import insideworld.engine.entities.Entity;
import java.util.Collection;

public interface SearchStorages {

    Collection<Class<? extends Entity>> search();

}
