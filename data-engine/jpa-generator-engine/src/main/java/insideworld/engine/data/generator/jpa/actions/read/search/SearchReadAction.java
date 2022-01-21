package insideworld.engine.data.generator.jpa.actions.read.search;

import insideworld.engine.data.generator.jpa.actions.read.annotations.GenerateReadAction;
import java.util.Collection;

public interface SearchReadAction {

    Collection<GenerateReadAction> search();

}
