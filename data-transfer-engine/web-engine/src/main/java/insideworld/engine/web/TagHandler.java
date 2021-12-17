package insideworld.engine.web;


import insideworld.engine.actions.ActionException;
import insideworld.engine.actions.keeper.context.Context;

/**
 * Tags handler for redefine.
 */
public interface TagHandler {

    void perform(Context context) throws ActionException;

}
