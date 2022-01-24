package insideworld.engine.data.generator.jpa.actions.delete.search;

import insideworld.engine.data.generator.jpa.actions.abstracts.info.ActionTagsInfo;
import insideworld.engine.data.generator.jpa.actions.delete.annotations.GenerateDeleteAction;
import java.util.Collection;

public interface SearchDeleteAction {

    Collection<ActionTagsInfo> search();

}
