package insideworld.engine.actions.facade;

import insideworld.engine.actions.Action;
import java.util.Collection;

/**
 * Using for change or add action.
 */
public interface ActionChanger {

   void addAction(final Action action);

   void addActions(final Collection<Action> actions);
}
