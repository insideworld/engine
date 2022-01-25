package insideworld.engine.data.generator.jpa.storage.search;

import insideworld.engine.data.generator.jpa.storage.annotations.GenerateCrud;
import insideworld.engine.entities.Entity;
import java.util.Collection;

public interface SearchStorages {

    Collection<StorageInfo> search();

}
