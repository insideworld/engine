package insideworld.engine.data.generator.jpa.actions.write.search;

import insideworld.engine.data.generator.jpa.actions.abstracts.info.ActionTagInfo;
import insideworld.engine.data.generator.jpa.actions.abstracts.info.ActionTagsInfo;
import java.util.Collection;

public interface SearchWriteAction {

    Collection<ActionTagInfo> search();

}
